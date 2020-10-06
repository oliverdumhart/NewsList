package com.oliverdumhart.moap.dummynewslist

import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.oliverdumhart.moap.dummynewslist.entities.NewsItem
import java.text.SimpleDateFormat

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (intent.hasExtra(ITEM_EXTRA)) {
            val titleTextView = findViewById<TextView>(R.id.title)
            val descriptionTextView = findViewById<TextView>(R.id.description)
            val authorTextView = findViewById<TextView>(R.id.author)
            val publicationDateTextView = findViewById<TextView>(R.id.publication_date)
            val keywordsTextView = findViewById<TextView>(R.id.keywords)
            val item: NewsItem? = intent.getParcelableExtra(ITEM_EXTRA)
            item?.let { item ->
                titleTextView.text = item.title ?: ""
                descriptionTextView.text = item.description ?: ""
                authorTextView.text = item.author ?: ""
                val sdf = SimpleDateFormat("dd.MM.yyyy")
                publicationDateTextView.text = item.publicationDate?.let { sdf.format(it) } ?: ""
                keywordsTextView.text = item.keywords?.joinToString("\n") ?: ""
            }
        }
    }

    companion object {
        const val ITEM_EXTRA = "ITEM_EXTRA"
    }
}