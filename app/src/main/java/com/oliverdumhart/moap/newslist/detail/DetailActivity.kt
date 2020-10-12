package com.oliverdumhart.moap.newslist.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.oliverdumhart.moap.newslist.R
import com.oliverdumhart.moap.newslist.entities.NewsItem
import com.oliverdumhart.moap.newslist.extensions.toString
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.news_item_top.*

class DetailActivity : AppCompatActivity() {
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setupViewModel()

        if (viewModel.showImages && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.getStringExtra(EXTRA_TRANSITION_NAME)?.let {
                ViewCompat.setTransitionName(image, it)
            }
        }

        full_story_button.setOnClickListener { viewModel.fullStoryButtonClicked() }

        viewModel.newsItem.let { item ->
            titleTextView.text = item.title ?: ""
            description.text = item.description ?: ""
            author.text = item.author ?: ""
            date.text = item.publicationDate?.toString("MMM dd, yyyy HH:mm")
                    ?: ""
            keywords.text = item.keywords?.joinToString(", ") ?: ""
            if (viewModel.showImages) {
                Glide.with(image.context).load(item.image).apply(RequestOptions().placeholder(R.drawable.loading_animation)).into(image)
            }
        }

        viewModel.eventShowLink.observe(this, { link ->
            if (link != "") {
                openLink(link)
                viewModel.eventShowLinkComplete()
            }
        })
    }

    private fun setupViewModel() {
        if (intent.hasExtra(ITEM_EXTRA)) {
            val item: NewsItem? = intent.getParcelableExtra(ITEM_EXTRA)

            if (item != null) {
                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                val showImages = prefs.getBoolean(getString(R.string.settings_display_images_key), resources.getBoolean(R.bool.pref_display_images_default))

                viewModel = ViewModelProvider(this, DetailViewModelFactory(item, showImages)).get(DetailViewModel::class.java)
            } else {
                showErrorMessage()
            }
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