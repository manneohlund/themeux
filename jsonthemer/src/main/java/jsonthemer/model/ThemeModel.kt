package jsonthemer.model

/**
 * Created by Manne Öhlund on 2018-02-27.
 * Copyright © 2018 Frost. All rights reserved.
 */
interface ThemeModel {
    var theme: Int
    var statusBarColor: String
    var navigationBarColor: String

    fun getModelTheme(): Int
    fun getStatusBarColor(): Int
    fun getToolbarColor(): Int
    fun getNavigationBarColor(): Int
}