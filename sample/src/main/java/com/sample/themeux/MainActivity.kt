package com.sample.themeux

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.sample.themeux.databinding.ActivityMainBinding
import jsonthemer.JsonThemer
import jsonthemer.model.ThemeModel

class MainActivity : AppCompatActivity() {

    companion object {
        val BLUE_THEME = "blue_theme.json"
        val GREEN_THEME = "green_theme.json"
        val LIME_THEME = "lime_theme.json"
        val RED_THEME = "red_theme.json"
        val WHITE_THEME = "white_theme.json"

        var currentTheme: String = LIME_THEME
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val theme: ThemeModel = let {
            try {
                JsonThemer.setup(this, currentTheme)
            } catch (e: Exception) {
                Log.e(MainActivity::class.simpleName, "Error: AssetFileNotFound, " + e.message)

                // Fallback on base model
                ThemeModel()
            }
        }
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.themeModel = theme

        // Set ActionBar from bindings
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_red) {
            changeTheme(RED_THEME)
        } else if (id == R.id.action_lime) {
            changeTheme(LIME_THEME)
        } else if (id == R.id.action_green) {
            changeTheme(GREEN_THEME)
        } else if (id == R.id.action_blue) {
            changeTheme(BLUE_THEME)
        } else if (id == R.id.action_white) {
            changeTheme(WHITE_THEME)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeTheme(style: String) {
        currentTheme = style
        recreate()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    //or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    //or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    //or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    or View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }
}