package com.androidapp.trendingrepoclient.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by S.Nur Uysal on 2020-01-21.
 */

data class GitResponse(
    @SerializedName("total_count")
    var totalCount: Int = 0,

    @SerializedName("incomplete_results")
    var incompleteResults: Boolean = false,

    @SerializedName("items")
    var items: List<Repository>
)