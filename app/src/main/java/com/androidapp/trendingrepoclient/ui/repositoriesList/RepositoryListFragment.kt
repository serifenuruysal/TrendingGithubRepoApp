package com.androidapp.trendingrepoclient.ui.repositoriesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidapp.trendingrepoclient.R
import com.androidapp.trendingrepoclient.rx.RxBus
import com.androidapp.trendingrepoclient.ui.event.RepositoryClickEvent
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.repository_list_fragment.*


/**
 * Created by S.Nur Uysal on 2020-01-26.
 */

class RepositoryListFragment : Fragment() {

    lateinit var adapter: RepositoryAdapter

    lateinit var subscribeArticleClickEvent: Disposable

    lateinit var repositoryListViewModel: RepositoryListViewModel

    companion object {
        fun newInstance(accessToken: String): RepositoryListFragment {
            val fragment = RepositoryListFragment()
            val arguments = Bundle()
            arguments.putString("accessToken", accessToken)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.repository_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var accessToken: String? = null
        arguments?.let {
             accessToken = arguments!!.getString("accessToken")
        }

        repositoryListViewModel =
            ViewModelProviders.of(this, RepositoryListViewModelFactory(context!!,accessToken)).get(
                RepositoryListViewModel::class.java
            )
        initRepositoriesList()
        initSubscriptions()

    }



    private fun initSubscriptions() {
        repositoryListViewModel.repositories.observe(this, Observer {

            adapter.submitList(it)
        })

        subscribeArticleClickEvent =
            RxBus.listen(RepositoryClickEvent::class.java).subscribe {

            }
    }

    private fun initRepositoriesList() {

        adapter = RepositoryAdapter()
        rv_repositories.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_repositories.adapter = adapter
    }


    override fun onDestroy() {
        super.onDestroy()
        if (!subscribeArticleClickEvent.isDisposed) {
            subscribeArticleClickEvent.dispose()
        }
    }
}