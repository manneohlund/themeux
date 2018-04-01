package com.sample.themeux.model

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
    var accentColor = 0xFF88C34A.toInt()

    @TaskDescriptionColor
    @StatusBarColor
    @PrimaryColor
    private val primaryColor = 0xFF4CAF50.toInt()

    @NavigationBarColor
    @PrimaryDarkColor
    private val primaryDarkColor = 0xC8388E3C.toInt()

    fun getPrimaryColorDrawable(): ColorDrawable {
        return ColorDrawable(primaryColor)
    }

    fun getPrimaryDarkColorDrawable(): ColorDrawable {
        return ColorDrawable(primaryDarkColor)
    }
}