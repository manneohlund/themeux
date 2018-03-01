package jsonthemer.model

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import jsonthemer.annotation.*

/**
 * Created by Manne Öhlund on 2018-02-23.
 * Copyright © 2018. All rights reserved.
 */

class BaseThemeModel {
    val LIGHT = 0
    val DARK = 1

    var theme = LIGHT
    @StatusBar
    var statusBarColor = "#FFFF5722"
    @NavigationBar
    var navigationBarColor = "#FFFF5722"

    // Theme overlays
    private var toolbarThemeOverlay: Int = LIGHT
    private var popupThemeOverlay: Int = LIGHT

    // Other
    @ToolbarBar
    private var toolbarColor: String = "#FFFF5722"
    @AccentColor
    private var accentColor: String = "#FFFF5722"
    private var windowBackgroundColor: String = "#FF000000"

    @Theme
    fun getModelTheme(): Int {
        return when (theme) {
            DARK -> return android.support.v7.appcompat.R.style.Theme_AppCompat_NoActionBar
            else -> android.support.v7.appcompat.R.style.Theme_AppCompat_Light_NoActionBar
        }
    }

    @ToolbarThemeOverlay
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

    fun getStatusBarColor(): Int {
        return Color.parseColor(statusBarColor)
    }

    fun getStatusBarColorDrawable(): ColorDrawable {
        return ColorDrawable(getStatusBarColor())
    }

    fun getToolbarColor(): Int {
        return Color.parseColor(toolbarColor)
    }

    fun getAccentColor(): Int {
        return Color.parseColor(accentColor)
    }

    fun getNavigationBarColor(): Int {
        return Color.parseColor(navigationBarColor)
    }

    fun getWindowBackgroundColor(): Int {
        return Color.parseColor(windowBackgroundColor)
    }
}