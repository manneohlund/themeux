package themeux.model

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
import themeux.annotation.ToolbarBarBackground
import themeux.annotation.color.AccentColor
import themeux.annotation.color.NavigationBarColor
import themeux.annotation.color.StatusBarColor
import themeux.annotation.color.TaskDescriptionColor
import themeux.annotation.flag.WindowFlags
import themeux.annotation.theme.Theme
import themeux.annotation.theme.ToolbarPopupThemeOverlay
import themeux.annotation.theme.ToolbarThemeOverlay

/**
 * Created by Manne Öhlund on 2018-02-23.
 * Copyright © 2018. All rights reserved.
 */

@WindowFlags(
        FLAG_LAYOUT_IN_SCREEN
        or FLAG_TRANSLUCENT_NAVIGATION
)
class ThemeModel {
    val LIGHT = 0
    val DARK = 1

    var theme = LIGHT
    @TaskDescriptionColor
    @StatusBarColor
    var statusBarColor = "#FFFF5722"
    @NavigationBarColor
    var navigationBarColor = "#FFFF5722"

    // Theme overlays
    private var toolbarThemeOverlay: Int = LIGHT
    private var popupThemeOverlay: Int = LIGHT

    // Other
    @ToolbarBarBackground
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
            else -> android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_Light
        }
    }

    @ToolbarPopupThemeOverlay
    fun getToolbarPopupThemeOverlay(): Int {
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

    fun getToolbarColorDrawable(): ColorDrawable {
        return ColorDrawable(getToolbarColor())
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