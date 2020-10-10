package com.oliverdumhart.moap.dummynewslist

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.oliverdumhart.moap.dummynewslist.entities.NewsItem
import com.oliverdumhart.moap.dummynewslist.extensions.toString
import java.text.SimpleDateFormat

class DetailActivity : AppCompatActivity() {
    private val viewModel: NewsItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (intent.hasExtra(ITEM_EXTRA)) {
            val titleTextView = findViewById<TextView>(R.id.title)
            val descriptionTextView = findViewById<TextView>(R.id.description)
            val authorTextView = findViewById<TextView>(R.id.author)
            val publicationDateTextView = findViewById<TextView>(R.id.date)
            val keywordsTextView = findViewById<TextView>(R.id.keywords)
            val imageView = findViewById<ImageView>(R.id.image)
            val fullStoryButton = findViewById<Button>(R.id.full_story_button)
            val item: NewsItem? = intent.getParcelableExtra(ITEM_EXTRA)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                intent.getStringExtra(EXTRA_TRANSITION_NAME)?.let {
                    ViewCompat.setTransitionName(imageView, it)
                }
            }

            if (item != null) {
                viewModel.updateNewsItem(item)
            } else {
                showErrorMessage()
            }

            fullStoryButton.setOnClickListener { viewModel.fullStoryButtonClicked() }

            viewModel.newsItem.observe(this, { item ->
                titleTextView.text = item.title ?: ""
                descriptionTextView.text = item.description ?: ""
                authorTextView.text = item.author ?: ""
                publicationDateTextView.text = item.publicationDate?.toString("MMM dd, yyyy HH:mm")
                        ?: ""
                keywordsTextView.text = item.keywords?.joinToString(", ") ?: ""
                Glide.with(imageView.context).load(item.image).into(imageView)
            })

            viewModel.eventShowLink.observe(this, { link ->
                if (link != "") {
                    openLink(link)
                    viewModel.eventShowLinkComplete()
                }
            })
        } else {
            showErrorMessage()
        }
    }

    fun openLink(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    fun showErrorMessage() {
        Toast.makeText(this, getString(R.string.detail_extra_error), Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val ITEM_EXTRA = "ITEM_EXTRA"
        const val EXTRA_TRANSITION_NAME = "EXTRA_TRANSITION_NAME"
    }
}