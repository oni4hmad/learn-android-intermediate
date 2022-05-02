package com.dicoding.picodiploma.storyapp1.ui.liststory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.dicoding.picodiploma.storyapp1.R
import com.dicoding.picodiploma.storyapp1.data.preferences.SessionPreference

class StoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)
        findNavController(R.id.nav_host_story).setGraph(R.navigation.story_navigation, intent.extras)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.story_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_story -> {
                Navigation.findNavController(this, R.id.nav_host_story).navigate(R.id.action_listStoryFragment_to_addStoryActivity)
                true
            }
            R.id.logout -> {
                SessionPreference(this).clearSession()
                Navigation.findNavController(this, R.id.nav_host_story).navigate(R.id.action_listStoryFragment_to_mainActivity)
                finish()
                true
            }
            R.id.change_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}