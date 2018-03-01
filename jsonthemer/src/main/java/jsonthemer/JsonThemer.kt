package jsonthemer

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import jsonthemer.annotation.NavigationBar
import jsonthemer.annotation.StatusBar
import jsonthemer.annotation.Theme
import jsonthemer.factory.DefaultThemeLayoutInflaterFactory
import jsonthemer.util.AssetsUtils
import jsonthemer.util.ReflectionUtil
import java.io.File
import java.io.IOException
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * Created by Manne Öhlund on 2018-02-27.
 * Copyright © 2018. All rights reserved.
 */

object JsonThemer {

    /**
     * Setup ThemeModel from assets file
     * @exception NoSuchFileException
     */
    @Throws(IOException::class)
    inline fun <reified T: Any> setup(activity: Activity, assetsFileName: String) : T {
        val themeModel: T = AssetsUtils.loadStyleFromAssets(activity, assetsFileName)
        return setup(activity, themeModel)
    }

    /**
     * Setup ThemeModel from file, internal or external storage
     * @exception Exception External storage will need runtime permissions
     * @exception NoSuchFileException
     */
    @Throws(IOException::class)
    inline fun <reified T: Any> setup(activity: Activity, file: File) : T {
        val themeModel: T = AssetsUtils.loadStyleFromFile(file)
        return setup(activity, themeModel)
    }

    /**
     * Setup ThemeModel from Java class
     */
    fun <T: Any> setup(activity: Activity, themeModel: Class<T>) : T {
        return setup(activity, themeModel.newInstance())
    }

    /**
     * Setup ThemeModel from Kotlin class
     */
    fun <T: Any> setup(activity: Activity, themeModel: KClass<T>) : T {
        return setup(activity, themeModel.createInstance())
    }

    fun <T: Any> setup(activity: Activity, themeModel: T) : T {
        setupTheme(activity, themeModel)
        setStatusBarAndNavigationBarColor(activity, themeModel)
        setLayoutInflater(activity, DefaultThemeLayoutInflaterFactory(activity, themeModel))
        return themeModel
    }

    fun setupTheme(activity: Activity, themeModel: Any) {
        val theme: Int = ReflectionUtil.getValue(themeModel, Theme::class) as Int
        activity.setTheme(theme)
    }

    fun setStatusBarAndNavigationBarColor(activity: Activity, themeModel: Any) {
        val statusBarColor = ReflectionUtil.getValue(themeModel, StatusBar::class).let {
            if (it is String) Color.parseColor(it) else it as Int
        }

        val navigationBarColor = ReflectionUtil.getValue(themeModel, NavigationBar::class).let {
            if (it is String) Color.parseColor(it) else it as Int
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = statusBarColor
            activity.window.navigationBarColor = navigationBarColor
        }
    }

    fun setLayoutInflater(activity: Activity, layoutInflaterFactory: LayoutInflater.Factory2) {
        activity.getLayoutInflater().setFactory2(layoutInflaterFactory);
    }
}
