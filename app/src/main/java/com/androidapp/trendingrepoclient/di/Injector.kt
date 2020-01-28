package com.androidapp.trendingrepoclient.di

import androidx.paging.DataSource
import com.androidapp.trendingrepoclient.data.GithubRepoService
import com.androidapp.trendingrepoclient.data.RepositoryService
import com.androidapp.trendingrepoclient.domain.datasource.RepositoryDataSourceFactory
import com.androidapp.trendingrepoclient.entity.Repository
import org.koin.dsl.module.module

/**
 * Created by S.Nur Uysal on 2020-01-26.
 */


val module = module {
    single { GithubRepoService(get()) as RepositoryService }

    single {
        RepositoryDataSourceFactory(get(),get()) as DataSource.Factory<Int, Repository>
    }

}