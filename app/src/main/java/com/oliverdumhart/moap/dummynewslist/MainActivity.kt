package com.oliverdumhart.moap.dummynewslist

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.oliverdumhart.moap.dummynewslist.DetailActivity.Companion.EXTRA_TRANSITION_NAME
import com.oliverdumhart.moap.dummynewslist.entities.NewsItem

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val viewModel: NewsListViewModel by viewModels()
    private lateinit var prefs: SharedPreferences
    private lateinit var url: String
    private lateinit var adapter: NewsItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = NewsItemAdapter(EXTRA_TRANSITION_NAME, NewsItemAdapter.NewsItemClickListener{ item, imageView ->
            showDetailActivity(item, imageView)
        })

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
        viewModel.loadNews(url)
        prefs.registerOnSharedPreferenceChangeListener(this)
    }

    private fun showDetailActivity(item: NewsItem, imageView: ImageView) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.ITEM_EXTRA, item)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
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
            viewModel.loadNews(url)
            true
        }
        R.id.action_settings -> {
            showSettings();
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun showSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == getString(R.string.settings_url_key)) {
            sharedPreferences?.let {
                url = it.getString(key, getString(R.string.settings_url_default))!!
                viewModel.loadNews(url)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }
}