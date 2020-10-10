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
import java.lang.IllegalArgumentException

class NewsItemAdapter(private val transitionName: String, private val clickListener: NewsItemClickListener, private var items: List<NewsItem> = listOf()) : RecyclerView.Adapter<NewsItemViewHolder>() {

    companion object {
        private val VIEW_TYPE_TOP = 0
        private val VIEW_TYPE_DEFAULT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layout = when (viewType) {
            VIEW_TYPE_DEFAULT -> R.layout.news_item
            VIEW_TYPE_TOP -> R.layout.news_item_top
            else -> throw IllegalArgumentException("No valid viewType specified")
        }
        val view = inflater.inflate(layout, parent, false)
        return NewsItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int = (if (position == 0) VIEW_TYPE_TOP else VIEW_TYPE_DEFAULT)

    fun updateList(newsList: List<NewsItem>) {
        items = newsList
        notifyDataSetChanged()
    }

    inner class NewsItemViewHolder(itemView: View) : ViewHolder(itemView) {
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