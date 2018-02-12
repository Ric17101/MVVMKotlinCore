package com.karntrehan.posts.details

import com.freelance.ric17.mvvmcore.extensions.*
import com.freelance.ric17.mvvmcore.networking.Outcome
import com.karntrehan.posts.commons.data.local.Comment
import com.karntrehan.posts.commons.data.local.PostDb
import com.karntrehan.posts.commons.data.remote.PostService
import com.karntrehan.posts.details.exceptions.DetailsExceptions
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class DetailsRepository(val postDb: PostDb, val postService: PostService) {

    val commentsFetchOutcome: PublishSubject<Outcome<List<Comment>>> = PublishSubject.create<Outcome<List<Comment>>>()

    var remoteFetch = true

    fun fetchCommentsFor(postId: Int?, compositeDisposable: CompositeDisposable) {
        if (postId == null)
            return

        commentsFetchOutcome.loading(true)
        postDb.commentDao().getForPost(postId)
                .performOnBackOutOnMain()
                .subscribe({ retailers ->
                    commentsFetchOutcome.success(retailers)
                    if (remoteFetch)
                        refreshComments(postId, compositeDisposable)
                    remoteFetch = false
                }, { error -> handleError(error) })
                .addTo(compositeDisposable)
    }

    fun refreshComments(postId: Int, compositeDisposable: CompositeDisposable) {
        commentsFetchOutcome.loading(true)
        postService.getComments(postId)
                .performOnBackOutOnMain()
                .subscribe({ comments -> saveCommentsForPost(comments) }, { error -> handleError(error) })
                .addTo(compositeDisposable)
    }

    private fun saveCommentsForPost(comments: List<Comment>) {
        if (comments.isNotEmpty()) {
            Completable.fromAction {
                postDb.commentDao().insertAll(comments)
            }
                    .performOnBackOutOnMain()
                    .subscribe()
        } else
            commentsFetchOutcome.failed(DetailsExceptions.NoCommentsException())
    }

    private fun handleError(error: Throwable) {
        commentsFetchOutcome.failed(error)
    }

}