package com.karntrehan.posts.details

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.freelance.ric17.mvvmcore.extensions.toLiveData
import com.freelance.ric17.mvvmcore.networking.Outcome
import com.karntrehan.posts.commons.PostDH
import com.karntrehan.posts.commons.data.local.Comment
import io.reactivex.disposables.CompositeDisposable

class DetailsViewModel(val repo: DetailsRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val commentsOutcome: LiveData<Outcome<List<Comment>>> by lazy {
        repo.commentsFetchOutcome.toLiveData(compositeDisposable)
    }

    fun loadCommentsFor(postId: Int?) {
        repo.fetchCommentsFor(postId, compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        //clear the disposables when the viewmodel is cleared
        compositeDisposable.clear()
        PostDH.destroyDetailsComponent()
    }

    fun refreshCommentsFor(postId: Int?) {
        if (postId != null)
            repo.refreshComments(postId, compositeDisposable)
    }
}