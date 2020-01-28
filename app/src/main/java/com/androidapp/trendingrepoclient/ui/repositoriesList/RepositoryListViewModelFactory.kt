package com.androidapp.trendingrepoclient.ui.repositoriesList

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by S.Nur Uysal on 2020-01-26.
 */
@Suppress("UNCHECKED_CAST")
class RepositoryListViewModelFactory(
    private val context: Context,
    private val accessToken: String?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RepositoryListViewModel(context = context, accessToken = accessToken) as T
    }
}