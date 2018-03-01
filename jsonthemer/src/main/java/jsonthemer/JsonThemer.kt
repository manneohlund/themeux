package jsonthemer

import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import jsonthemer.annotation.NavigationBar
import jsonthemer.annotation.StatusBar
import jsonthemer.annotation.Theme
import jsonthemer.annotation.ToolbarBar
import jsonthemer.factory.DefaultThemeLayoutInflaterFactory
import jsonthemer.util.AssetsUtils
import jsonthemer.util.ReflectionUtil
import kotlin.reflect.full.declaredMembers

/**
 * Created by Manne Öhlund on 2018-02-27.
 * Copyright © 2018. All rights reserved.
 */

object JsonThemer {
    inline fun <reified T: Any> setup(activity: AppCompatActivity, assetsFileName: String) : T {
        val themeModel: T = AssetsUtils.loadStyleAssets(activity, assetsFileName)

        val targetAnnotations = listOf(Theme::class, StatusBar::class, NavigationBar::class, ToolbarBar::class)
        val params = themeModel.javaClass.kotlin.declaredMembers.filter { it.annotations.any { annotation -> targetAnnotations.contains(annotation.annotationClass) } }

        setupTheme(activity, themeModel)
        setStatusBarAndNavigationBarColor(activity, themeModel)
        setLayoutInflater(activity, DefaultThemeLayoutInflaterFactory(activity, themeModel))

        return themeModel
    }

    fun setupTheme(activity: AppCompatActivity, themeModel: Any) {
        //throw Exception("Theme not found! Add @Theme annotation to only one field or function")
        val theme: Int = ReflectionUtil.getValue(themeModel, Theme::class) as Int
        activity.setTheme(theme)
    }

    fun setStatusBarAndNavigationBarColor(activity: AppCompatActivity, themeModel: Any) {
        var statusBarColor = ReflectionUtil.getValue(themeModel, StatusBar::class)
        if (statusBarColor is String) statusBarColor = Color.parseColor(statusBarColor)

        var navigationBarColor = ReflectionUtil.getValue(themeModel, NavigationBar::class)
        if (navigationBarColor is String) navigationBarColor = Color.parseColor(navigationBarColor)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = statusBarColor as Int
            activity.window.navigationBarColor = navigationBarColor as Int
        }
    }

    fun setLayoutInflater(activity: AppCompatActivity, layoutInflaterFactory: LayoutInflater.Factory2) {
        activity.getLayoutInflater().setFactory2(layoutInflaterFactory);
    }
}
