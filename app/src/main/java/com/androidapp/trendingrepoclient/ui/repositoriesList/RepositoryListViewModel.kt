package com.androidapp.trendingrepoclient.ui.repositoriesList

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.androidapp.trendingrepoclient.data.db.GithubDb
import com.androidapp.trendingrepoclient.data.db.GithubDbProvider
import com.androidapp.trendingrepoclient.domain.datasource.RepositoryDataSourceFactory
import com.androidapp.trendingrepoclient.domain.datasource.RepositoryPagedDataSource
import com.androidapp.trendingrepoclient.entity.Repository


/**
 * Created by S.Nur Uysal on 2020-01-26.
 */
class RepositoryListViewModel(context: Context, accessToken: String?) : ViewModel() {
    private var githubDb: GithubDb
    var repositories: LiveData<PagedList<Repository>>
    private var liveDataSource: MutableLiveData<RepositoryPagedDataSource>? = null

    private var itemDataSourceFactory: RepositoryDataSourceFactory

    init {

        val app = context.applicationContext as Application
        githubDb= GithubDbProvider.getInstance(app)


        itemDataSourceFactory = RepositoryDataSourceFactory(githubDb, accessToken)

        //getting the live data source from data source factory
        liveDataSource = itemDataSourceFactory.postLiveData

        //Getting PagedList config
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10).build()

        //Building the paged list
        repositories = LivePagedListBuilder(itemDataSourceFactory, pagedListConfig).build()

    }


}