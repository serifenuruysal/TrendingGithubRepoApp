package com.androidapp.trendingrepoclient.domain.datasource

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.androidapp.trendingrepoclient.data.GithubRepoService
import com.androidapp.trendingrepoclient.data.db.GithubDb
import com.androidapp.trendingrepoclient.entity.GitResponse
import com.androidapp.trendingrepoclient.entity.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by S.Nur Uysal on 2019-11-03.
 */


class RepositoryPagedDataSource(private val githubDb: GithubDb, accessToken: String?) :
    PageKeyedDataSource<Int, Repository>() {
    private val ioExecutor: Executor = Executors.newSingleThreadExecutor()
    private val repositoryDao = githubDb.repositories()
    private val service = GithubRepoService(accessToken)

    init {
        ioExecutor.execute {
            githubDb.runInTransaction {
                githubDb.repositories().deleteAll()
            }
        }

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repository>) {

        val call = service.getTrendingRepos(params.key, ITEM_COUNT)
        call.enqueue(object : Callback<GitResponse> {
            override fun onResponse(call: Call<GitResponse>, response: Response<GitResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()!!
                    val responseItems = apiResponse.items
                    val key = if (params.key > 1) params.key - 1 else 0
                    responseItems.let {
                        //                        Log.d("serife", "loadBefore getTrendingRepos")

                        callback.onResult(responseItems, key)

                        insertResultIntoDb(responseItems, params.key)
                    }
                }
            }

            override fun onFailure(call: Call<GitResponse>, t: Throwable) {
//                Log.d("serife", "onFailure")
            }
        })
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Repository>
    ) {
        ioExecutor.execute {
            githubDb.runInTransaction {
                githubDb.repositories().deleteAll()
            }
        }

        val call = service.getTrendingRepos(FIRST_PAGE, ITEM_COUNT)
        call.enqueue(object : Callback<GitResponse> {
            override fun onResponse(call: Call<GitResponse>, response: Response<GitResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()!!
                    val responseItems = apiResponse.items
                    responseItems.let {
                        //                        Log.d("serife", "loadInitial getTrendingRepos")
                        callback.onResult(responseItems, null, FIRST_PAGE + 1)
                        insertResultIntoDb(responseItems, FIRST_PAGE)
                    }
                }
            }

            override fun onFailure(call: Call<GitResponse>, t: Throwable) {
//                Log.d("serife", "onFailure")
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repository>) {
        val call = service.getTrendingRepos(params.key, ITEM_COUNT)
        call.enqueue(object : Callback<GitResponse> {
            override fun onResponse(call: Call<GitResponse>, response: Response<GitResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()!!
                    val responseItems = apiResponse.items
                    val key = params.key + 1
                    responseItems.let {
                        //                        Log.d("serife", "loadAfter getTrendingRepos")
                        callback.onResult(responseItems, key)
                        insertResultIntoDb(responseItems, params.key)
                    }
                }
            }

            override fun onFailure(call: Call<GitResponse>, t: Throwable) {
//                Log.d("serife", "onFailure")
            }
        })
    }

    private fun insertResultIntoDb(list: List<Repository>, page: Int) {
        ioExecutor.execute {
            githubDb.runInTransaction {
//                Log.d("serife", /"list size:${list.size}")
                list.forEach {
                    if (repositoryDao.repositoryBySubGithubId(it.id) == null) {
//                        Log.d("serife", "insertRepo${it.id}")
                        it.pageNum = page
                        repositoryDao.insertRepo(it)
                    }
//                    else{
//                        Log.d("serife", "exist at repo${it.id}")
//                    }

                }
            }
        }

    }


    companion object {
        const val ITEM_COUNT = 10
        const val FIRST_PAGE = 1

    }
}