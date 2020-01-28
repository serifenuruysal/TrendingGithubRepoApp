package com.androidapp.trendingrepoclient.ui.repositoriesList


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androidapp.trendingrepoclient.R
import com.androidapp.trendingrepoclient.entity.Repository
import com.androidapp.trendingrepoclient.rx.RxBus
import com.androidapp.trendingrepoclient.ui.event.RepositoryClickEvent
import kotlinx.android.synthetic.main.item_repository.view.*

/**
 * Created by S.Nur Uysal on 2020-01-26.
 */
class RepositoryAdapter :
    PagedListAdapter<Repository, RepositoryAdapter.RepositoryViewHolder>(RepositoryDiff) {

    class RepositoryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(repository: Repository) {
            view.tv_title.text = repository.name
            view.tv_sub_title.text = repository.language
            view.tv_description.text = repository.description
//            view.descriptionTextView.text = repository.description
//            view.sourceTextView.text = repository.source?.name
//            view.publishedAtTextView.text = repository.publishedAt
//
//            Glide.with(view.context)
//                .load(repository.urlToImage)
//                .placeholder(R.drawable.ic_picture)
//                .into(view.ivRepository)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repository, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        var repository = getItem(position)

        repository
            ?.let { holder.bind(it) }
        holder.view.setOnClickListener {
            RxBus.publish(RepositoryClickEvent(repository!!))
        }
    }

}

private object RepositoryDiff : DiffUtil.ItemCallback<Repository>() {

    override fun areItemsTheSame(oldItem: Repository, newItem: Repository) =
        oldItem.id.equals(newItem.id)

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        // In this case, if items are the same then content will always be the same
        return true
    }

}

