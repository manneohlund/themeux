package com.sample.themeux

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.sample.themeux.databinding.ActivityMainBinding
import themeux.Themeux
import themeux.model.ThemeModel

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
        val theme: ThemeModel = Themeux.setup(this, currentTheme)
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
}