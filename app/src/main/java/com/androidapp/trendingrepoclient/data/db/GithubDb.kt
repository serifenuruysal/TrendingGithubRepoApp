package com.androidapp.trendingrepoclient.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.androidapp.trendingrepoclient.entity.Repository
import com.androidapp.trendingrepoclient.entity.Token

/**
 * Created by S.Nur Uysal on 2020-01-27.
 */

/**
 * Database schema used by the DbGithubPostRepository
 */
@Database(
    entities = [Repository::class, Token::class],
    version = 1,
    exportSchema = true
)
abstract class GithubDb : RoomDatabase() {
    companion object {
        fun getInstance(context: Context): GithubDb {
            val databaseBuilder =
                Room.databaseBuilder(context, GithubDb::class.java, "githubb.d")

            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun repositories(): RepositoryDao

    abstract fun token(): TokenDao
}

object GithubDbProvider {

    private var githubDb: GithubDb? = null

    fun getInstance(context: Context): GithubDb {
        if (githubDb == null) {
            githubDb = GithubDb.getInstance(context)
        }
        return githubDb!!
    }

}