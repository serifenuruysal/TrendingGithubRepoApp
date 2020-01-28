package com.androidapp.trendingrepoclient.data.db

import androidx.room.*
import com.androidapp.trendingrepoclient.entity.Token

/**
 * Created by S.Nur Uysal on 2020-01-27.
 */

@Dao
interface TokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(token: Token)

    @Delete
    fun delete(token: Token)

    @Query("DELETE FROM token")
    fun deleteAll()

    @Query("SELECT * FROM token")
    fun getToken(): List<Token>?

}
