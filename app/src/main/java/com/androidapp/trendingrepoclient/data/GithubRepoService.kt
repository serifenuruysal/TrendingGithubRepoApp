package com.androidapp.trendingrepoclient.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

/**
 * Created by S.Nur Uysal on 2019-11-03.
 */


private const val GITHUB_API_BASE_URL = "https://api.github.com/"

class GithubRepoService(private val accessToken: String?) : RepositoryService {

    private val api: GithubApi

    init {
        api = Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                        .add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .client(providesOkHttpClient())
            .baseUrl(GITHUB_API_BASE_URL)
            .build()
            .create(GithubApi::class.java)
    }

    private fun providesOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(OAuthInterceptor(accessToken))
            .build()
    }

    override fun getTrendingRepos(page: Int, perpage: Int) =
        api.getRepositories(page, perpage)


}

class OAuthInterceptor(private val accessToken: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        if (accessToken != null)
            request = request.newBuilder().header("Authorization", "$accessToken").build()

        return chain.proceed(request)
    }
}