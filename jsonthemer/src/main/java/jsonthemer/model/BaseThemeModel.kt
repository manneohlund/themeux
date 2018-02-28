package jsonthemer.model

import android.graphics.Color
import android.graphics.drawable.ColorDrawable

/**
 * Created by Manne Öhlund on 2018-02-23.
 * Copyright © 2018 Frost. All rights reserved.
 */

class BaseThemeModel : ThemeModel {
    val LIGHT = 0
    val DARK = 1

    override var theme: Int = LIGHT
    override var statusBarColor: String = "#FFFF5722"
    override var navigationBarColor: String = "#FFFF5722"

    // Theme overlays
    private var toolbarThemeOverlay: Int = LIGHT
    private var popupThemeOverlay: Int = LIGHT

    // Other
    private var toolbarColor: String = "#FFFF5722"
    private var accentColor: String = "#FFFF5722"
    private var windowBackgroundColor: String = "#FF000000"

    override fun getModelTheme(): Int {
        return when (theme) {
            DARK -> return android.support.v7.appcompat.R.style.Theme_AppCompat_NoActionBar
            else -> android.support.v7.appcompat.R.style.Theme_AppCompat_Light_NoActionBar
        }
    }

    fun getToolbarThemeOverlay(): Int {
        return when (toolbarThemeOverlay) {
            DARK -> return android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_Dark_ActionBar
            else -> android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_ActionBar
        }
    }

    fun getPopupThemeOverlay(): Int {
        return when (popupThemeOverlay) {
            DARK -> return android.support.v7.appcompat.R.style.Theme_AppCompat
            else -> android.support.v7.appcompat.R.style.Theme_AppCompat_Light
        }
    }

    override fun getStatusBarColor(): Int {
        return Color.parseColor(statusBarColor)
    }
    fun getStatusBarColorDrawable(): ColorDrawable {
        return ColorDrawable(getStatusBarColor())
    }
    override fun getToolbarColor(): Int {
        return Color.parseColor(toolbarColor)
    }
    fun getAccentColor(): Int {
        return Color.parseColor(accentColor)
    }
    override fun getNavigationBarColor(): Int {
        return Color.parseColor(navigationBarColor)
    }
    fun getWindowBackgroundColor(): Int {
        return Color.parseColor(windowBackgroundColor)
    }
}