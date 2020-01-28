package com.androidapp.trendingrepoclient.data

import com.androidapp.trendingrepoclient.entity.GitResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by S.Nur Uysal on 2020-01-26.
 */

interface GithubApi {

    @GET("search/repositories?q=android%20&sort=stars&order=desc?client_id=9eda4a2e03a7e75bb5dd&client_secret=7f416b999a5950815475757750388cd13ab5f4fd")
    fun getRepositories(@Query("page") page: Int, @Query("per_page") per_page: Int): Call<GitResponse>
}