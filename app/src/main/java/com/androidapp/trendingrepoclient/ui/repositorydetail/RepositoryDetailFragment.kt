package com.androidapp.trendingrepoclient.ui.repositorydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidapp.trendingrepoclient.R
import com.androidapp.trendingrepoclient.entity.Repository
import kotlinx.android.synthetic.main.repository_detail_fragment.*
import kotlinx.android.synthetic.main.repository_detail_fragment.toolbar
import kotlinx.android.synthetic.main.repository_list_fragment.*

/**
 * Created by S.Nur Uysal on 2020-01-28.
 */

class RepositoryDetailFragment : Fragment() {

    private var repository: Repository? = null

    companion object {
        fun newInstance(repository: Repository): RepositoryDetailFragment {
            val fragment = RepositoryDetailFragment()
            val arguments = Bundle()
            arguments.putParcelable("repository", repository)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.repository_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()

        arguments?.let {
            repository = arguments!!.getParcelable("repository")
        }

        repository?.let {
            tv_title.text = repository?.name
            tv_sub_title.text = repository?.language
            tv_description.text = repository?.description
            tv_url.text = repository?.url
            tv_fork_count.text = getString(R.string.title_fork_count, repository?.forks!!)
            tv_open_issue_count.text =
                getString(R.string.title_open_issue_count, repository?.open_issues!!)
            tv_watchers_count.text = getString(R.string.title_watcher_count, repository?.watchers!!)
            tv_score.text = getString(R.string.title_score_count, repository?.score.toString())

        }


    }

    private fun setToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }
}