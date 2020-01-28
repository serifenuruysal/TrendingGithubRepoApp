package com.androidapp.trendingrepoclient.entity
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "repository",
    indices = [Index(value = ["id"], unique = false)]
)
@Parcelize
data class Repository(

    @SerializedName("language")
    var language: String? = null,

    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("name")
    var name: String? = null,


    @SerializedName("description")
    var description: String? = null,

    @SerializedName("html_url")
    var url: String? = null,

    @SerializedName("forks")
    var forks: Int? = 0,


    @SerializedName("score")
    var score: Double? = 0.0,

    @SerializedName("open_issues")
    var open_issues: Int? = 0,

    @SerializedName("watchers")
    var watchers: Int? = 0,

    var pageNum: Int = 0
):Parcelable