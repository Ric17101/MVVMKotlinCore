package com.karntrehan.posts.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.freelance.ric17.mvvmcore.extensions.toLiveData
import com.freelance.ric17.mvvmcore.networking.Outcome
import com.karntrehan.posts.commons.data.PostWithUser
import com.karntrehan.posts.commons.PostDH
import io.reactivex.disposables.CompositeDisposable

class ListViewModel(private val repo: ListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val TAG = "ListViewModel"

    val postsOutcome: LiveData<Outcome<List<PostWithUser>>> by lazy {
        //Convert publish subject to livedata
        repo.postFetchOutcome.toLiveData(compositeDisposable)
    }

    fun refreshPosts() {
        repo.refreshPosts(compositeDisposable)
    }

    fun getPosts() {
        if (postsOutcome.value == null)
            repo.fetchPosts(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        //clear the disposables when the viewmodel is cleared
        compositeDisposable.clear()
        PostDH.destroyListComponent()
    }
}