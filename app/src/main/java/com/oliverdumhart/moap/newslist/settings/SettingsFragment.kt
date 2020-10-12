package com.oliverdumhart.moap.newslist.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.oliverdumhart.moap.newslist.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_general)
    }

}