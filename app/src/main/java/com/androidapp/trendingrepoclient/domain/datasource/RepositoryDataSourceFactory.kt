package com.androidapp.trendingrepoclient.domain.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.androidapp.trendingrepoclient.data.db.GithubDb
import com.androidapp.trendingrepoclient.entity.Repository

/**
 * Created by S.Nur Uysal on 2020-01-26.
 */

class RepositoryDataSourceFactory(private val githubDb: GithubDb, private val accessToken: String?) :
    DataSource.Factory<Int, Repository>() {

    var postLiveData = MutableLiveData<RepositoryPagedDataSource>()

    override fun create(): DataSource<Int, Repository> {
        val dataSource =
            RepositoryPagedDataSource(githubDb, accessToken)

        // Keep reference to the data source with a MutableLiveData reference
        postLiveData.postValue(dataSource)

        return dataSource
    }

}