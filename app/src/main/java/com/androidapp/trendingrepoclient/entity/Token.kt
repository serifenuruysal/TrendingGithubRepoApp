package com.androidapp.trendingrepoclient.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by S.Nur Uysal on 2020-01-27.
 */

@Entity(
    tableName = "token",
    indices = [Index(value = ["id"], unique = true)]
)
data class Token(

    @PrimaryKey
    var id: String

)