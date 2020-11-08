package com.oliverdumhart.moap.newslist.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.oliverdumhart.moap.newslist.NotificationUtils
import com.oliverdumhart.moap.newslist.R
import com.oliverdumhart.moap.newslist.detail.DetailActivity
import com.oliverdumhart.moap.newslist.detail.DetailActivity.Companion.EXTRA_TRANSITION_NAME
import com.oliverdumhart.moap.newslist.entities.NewsItem
import com.oliverdumhart.moap.newslist.settings.SettingsActivity
import timber.log.Timber

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var prefs: SharedPreferences
    private lateinit var url: String
    private lateinit var adapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = NewsListAdapter(EXTRA_TRANSITION_NAME, NewsListAdapter.NewsItemClickListener { item, imageView ->
            showDetailActivity(item, imageView)
        })

        NotificationUtils.setupNotificationChannel(this)
        Timber.plant(Timber.DebugTree())
        findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            this.adapter = this@MainActivity.adapter
        }

        viewModel.newsList.observe(this) { newsList ->
            if (newsList != null) {
                adapter.updateList(newsList)
            }
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        url = prefs.getString(getString(R.string.settings_url_key), getString(R.string.settings_url_default))!!
        val showImages = prefs.getBoolean(getString(R.string.settings_display_images_key), resources.getBoolean(R.bool.pref_display_images_default))
        viewModel.setShowImages(showImages)
        prefs.registerOnSharedPreferenceChangeListener(this)

        viewModel.showImages.observe(this, {
            adapter.showImages = it
        })
    }

    private fun showDetailActivity(item: NewsItem, imageView: ImageView) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.ITEM_EXTRA, item)

        if (viewModel.showImages.value != false && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            intent.putExtra(EXTRA_TRANSITION_NAME, ViewCompat.getTransitionName(imageView))
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, ViewCompat.getTransitionName(imageView)!!)
            startActivity(intent, options.toBundle())
        } else {
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_reload -> {
            viewModel.reloadNews(url, ::handleError)
            true
        }
        R.id.action_settings -> {
            showSettings();
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun handleError(error: MainViewModel.Error) = when (error) {
        MainViewModel.Error.General ->
            Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_SHORT).show()
        MainViewModel.Error.Insert ->
            Toast.makeText(this, getString(R.string.insert_error), Toast.LENGTH_SHORT).show()
    }

    fun showSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            getString(R.string.settings_url_key) -> {
                sharedPreferences?.let {
                    url = it.getString(key, getString(R.string.settings_url_default))!!
                    viewModel.reloadNews(url, ::handleError)
                }
            }
            getString(R.string.settings_display_images_key) -> {
                sharedPreferences?.let {
                    val showImages = it.getBoolean(key, resources.getBoolean(R.bool.pref_display_images_default))
                    viewModel.setShowImages(showImages)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }
}