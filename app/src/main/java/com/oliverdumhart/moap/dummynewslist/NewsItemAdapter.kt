package com.oliverdumhart.moap.dummynewslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.oliverdumhart.moap.dummynewslist.NewsItemAdapter.NewsItemViewHolder
import com.oliverdumhart.moap.dummynewslist.entities.NewsItem
import com.oliverdumhart.moap.dummynewslist.extensions.toString

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
        private val image: ImageView
        private val author: TextView
        private val date: TextView

        fun bind(item: NewsItem) {
            title.text = item.title
            author.text = item.author
            date.text = item.publicationDate?.toString("MMM dd, yyyy, HH:mm") ?: ""
            item.image?.let {
                Glide.with(image.context).load(it).into(image)
            }
        }

        override fun onClick(view: View) {
            val item = items[adapterPosition]
            clickListener.onNewsItemClicked(item)
        }

        init {
            title = itemView.findViewById(R.id.title)
            image = itemView.findViewById(R.id.image)
            author = itemView.findViewById(R.id.author)
            date = itemView.findViewById(R.id.date)
            itemView.setOnClickListener(this)
        }
    }

    class NewsItemClickListener(val clickListener: (item: NewsItem) -> Unit) {
        fun onNewsItemClicked(item: NewsItem) = clickListener(item)
    }
}