package com.androidapp.trendingrepoclient.data.db

import androidx.paging.DataSource
import androidx.room.*
import com.androidapp.trendingrepoclient.entity.Repository

/**
 * Created by S.Nur Uysal on 2020-01-27.
 */

@Dao
interface RepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repos: List<Repository>)

    @Insert
    fun insertRepo(repo: Repository)

    @Delete
    fun delete(repos: List<Repository>)

    @Query("DELETE FROM repository")
    fun deleteAll()

    @Query("SELECT * FROM repository WHERE id = :id")
    fun repositoryBySubGithubId(id: Int): DataSource.Factory<Int?, Repository?>?

    @Query("SELECT * FROM repository ")
    fun loadAllRepo(): DataSource.Factory<Int?, Repository?>?
}
