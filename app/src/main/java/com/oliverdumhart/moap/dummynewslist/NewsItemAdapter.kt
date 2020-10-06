package com.oliverdumhart.moap.dummynewslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.oliverdumhart.moap.dummynewslist.NewsItemAdapter.NewsItemViewHolder
import com.oliverdumhart.moap.dummynewslist.entities.NewsItem

class NewsItemAdapter(private val clickListener: NewsItemClickListener, private var items: List<NewsItem> = listOf()) : RecyclerView.Adapter<NewsItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.news_item, parent, false)
        return NewsItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateList(newsList: List<NewsItem>) {
        items = newsList
        notifyDataSetChanged()
    }

    inner class NewsItemViewHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        private val title: TextView

        fun bind(item: NewsItem) {
            title.text = item.title
        }

        override fun onClick(view: View) {
            val item = items[adapterPosition]
            clickListener.onNewsItemClicked(item)
        }

        init {
            title = itemView.findViewById(R.id.title)
            itemView.setOnClickListener(this)
        }
    }

    class NewsItemClickListener(val clickListener: (item: NewsItem) -> Unit) {
        fun onNewsItemClicked(item: NewsItem) = clickListener(item)
    }
}