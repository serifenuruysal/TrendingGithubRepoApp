package com.androidapp.trendingrepoclient.data

import com.androidapp.trendingrepoclient.entity.GitResponse
import retrofit2.Call


/**
 * Created by S.Nur Uysal on 2019-11-03.
 */

private const val DEFAULT_HEADLINES_PAGE = 0
private const val DEFAULT_HEADLINES_PER_PAGE = 10

interface RepositoryService {

    fun getTrendingRepos(
        page: Int = DEFAULT_HEADLINES_PAGE, per_page: Int = DEFAULT_HEADLINES_PER_PAGE
    ): Call<GitResponse>
}