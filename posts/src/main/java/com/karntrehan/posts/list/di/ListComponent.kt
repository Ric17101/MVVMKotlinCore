package com.karntrehan.posts.list.di

import android.arch.persistence.room.Room
import android.content.Context
import com.freelance.ric17.mvvmcore.constants.Constants
import com.freelance.ric17.mvvmcore.di.CoreComponent
import com.karntrehan.posts.list.ListActivity
import com.karntrehan.posts.list.ListAdapter
import com.karntrehan.posts.list.ListRepository
import com.karntrehan.posts.list.ListViewModelFactory
import com.karntrehan.posts.commons.data.local.PostDb
import com.karntrehan.posts.commons.data.remote.PostService
import com.squareup.picasso.Picasso
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@ListScope
@Component(dependencies = [CoreComponent::class], modules = [ListModule::class])
interface ListComponent {

    //Expose to dependent components
    fun postDb(): PostDb

    fun postService(): PostService

    fun picasso(): Picasso

    fun inject(listActivity: ListActivity)
}

@Module
@ListScope
class ListModule {

    /*Adapter*/
    @Provides
    @ListScope
    fun adapter(picasso: Picasso): ListAdapter = ListAdapter(picasso)

    /*ViewModel*/
    @Provides
    @ListScope
    fun listViewModelFactory(listRepository: ListRepository): ListViewModelFactory {
        return ListViewModelFactory(listRepository)
    }

    /*Repository*/
    @Provides
    @ListScope
    fun listRepo(postDb: PostDb, postService: PostService): ListRepository = ListRepository(postDb, postService)

    @Provides
    @ListScope
    fun postDb(context: Context): PostDb = Room.databaseBuilder(context, PostDb::class.java, Constants.Posts.DB_NAME).build()

    @Provides
    @ListScope
    fun postService(retrofit: Retrofit): PostService = retrofit.create(PostService::class.java)
}