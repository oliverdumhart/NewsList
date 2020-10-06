package com.oliverdumhart.moap.dummynewslist

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener{
    private val viewModel: NewsListViewModel by viewModels()
    private lateinit var prefs: SharedPreferences
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = NewsItemAdapter(NewsItemAdapter.NewsItemClickListener { item ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.ITEM_EXTRA, item)
            startActivity(intent)
        })
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter

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

    fun showSettings(){
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if(key == getString(R.string.settings_url_key)){
            sharedPreferences?.let{
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