package com.sample.themeux.model

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import themeux.annotation.color.*
import themeux.annotation.theme.Theme
import themeux.annotation.theme.ToolbarPopupThemeOverlay
import themeux.annotation.theme.ToolbarThemeOverlay

/**
 * Created by Manne Öhlund on 2018-02-23.
 * Copyright © 2018. All rights reserved.
 */

class BasicThemeModel {
    @Theme
    val theme = android.support.v7.appcompat.R.style.Theme_AppCompat_NoActionBar

    @ToolbarThemeOverlay
    val toolbarThemeOverlay = android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_Dark

    @ToolbarPopupThemeOverlay
    val popupThemeOverlay = android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_Dark

    @AccentColor
    private val accentColor = Color.parseColor("#8BC34A")

    @TaskDescriptionColor
    @StatusBarColor
    @PrimaryColor
    private val primaryColor = Color.parseColor("#4CAF50")

    @PrimaryDarkColor
    private val primaryDarkColor = Color.parseColor("#C8388E3C")

    fun getPrimaryColorDrawable(): ColorDrawable {
        return ColorDrawable(primaryColor)
    }

    fun getPrimaryDarkColorDrawable(): ColorDrawable {
        return ColorDrawable(primaryDarkColor)
    }
}