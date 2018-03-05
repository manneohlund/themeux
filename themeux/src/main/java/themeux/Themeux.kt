package themeux

import android.app.Activity
import android.app.ActivityManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import themeux.annotation.color.NavigationBarColor
import themeux.annotation.color.StatusBarColor
import themeux.annotation.color.TaskDescriptionColor
import themeux.annotation.flag.WindowFlags
import themeux.annotation.theme.Theme
import themeux.factory.DefaultThemeLayoutInflaterFactory
import themeux.util.AssetsUtils
import themeux.util.ReflectionUtil
import java.io.File
import java.io.IOException
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation


/**
 * Created by Manne Öhlund on 2018-02-27.
 * Copyright © 2018. All rights reserved.
 */

object Themeux {

    /**
     * Setup ThemeModel from assets file
     * @exception NoSuchFileException
     */
    @Throws(IOException::class)
    inline fun <reified T: Any> setup(activity: Activity, assetsFileName: String, setDefaultThemeLayoutInflaterFactory: Boolean = true) : T {
        val themeModel: T = AssetsUtils.loadStyleFromAssets(activity, assetsFileName)
        return setup(activity, themeModel, setDefaultThemeLayoutInflaterFactory)
    }

    /**
     * Setup ThemeModel from file, internal or external storage
     * @exception Exception External storage will need runtime permissions
     * @exception NoSuchFileException
     */
    @Throws(IOException::class)
    inline fun <reified T: Any> setup(activity: Activity, file: File, setDefaultThemeLayoutInflaterFactory: Boolean = true) : T {
        val themeModel: T = AssetsUtils.loadStyleFromFile(file)
        return setup(activity, themeModel, setDefaultThemeLayoutInflaterFactory)
    }

    /**
     * Setup ThemeModel from Java class
     */
    fun <T: Any> setup(activity: Activity, themeModel: Class<T>, setDefaultThemeLayoutInflaterFactory: Boolean = true) : T {
        return setup(activity, themeModel.newInstance(), setDefaultThemeLayoutInflaterFactory)
    }

    /**
     * Setup ThemeModel from Kotlin class
     */
    fun <T: Any> setup(activity: Activity, themeModel: KClass<T>, setDefaultThemeLayoutInflaterFactory: Boolean = true) : T {
        return setup(activity, themeModel.createInstance(), setDefaultThemeLayoutInflaterFactory)
    }

    fun <T: Any> setup(activity: Activity, themeModel: T, setDefaultThemeLayoutInflaterFactory: Boolean = true) : T {
        setupWindowFlags(activity, themeModel)
        setupTheme(activity, themeModel)
        setupTaskDescription(activity, themeModel)
        setStatusBarAndNavigationBarColor(activity, themeModel)
        if (setDefaultThemeLayoutInflaterFactory) {
            setLayoutInflater(activity, DefaultThemeLayoutInflaterFactory(activity, themeModel))
        }
        return themeModel
    }

    fun setupWindowFlags(activity: Activity, themeModel: Any) {
        themeModel.javaClass.kotlin.findAnnotation<WindowFlags>().let { window ->
            if (window != null) {
                activity.window.addFlags(window.flags)
                assertWindowFlags(window.flags, themeModel)
            }
        }
    }

    fun setupTheme(activity: Activity, themeModel: Any) {
        val theme: Int = ReflectionUtil.getValue(themeModel, Theme::class) as Int
        activity.setTheme(theme)
    }

    fun setTaskDescription(activity: Activity, themeModel: Any, taskLabel: String? = null, taskIcon: Int? = null) {
        setupTaskDescription(activity, themeModel, taskLabel, taskIcon?.let { BitmapFactory.decodeResource(activity.resources, it) })
    }

    fun setTaskDescription(activity: Activity, themeModel: Any, taskLabel: String? = null, taskIcon: Bitmap? = null) {
        setupTaskDescription(activity, themeModel, taskLabel, taskIcon)
    }

    private fun setupTaskDescription(activity: Activity, themeModel: Any, taskLabel: String? = null, taskIcon: Bitmap? = null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getThemeModelColor(themeModel, TaskDescriptionColor::class, true)?.let { color ->
                val taskDescription = ActivityManager.TaskDescription(taskLabel, taskIcon, color)
                activity.setTaskDescription(taskDescription)
            }
        }
    }

    fun setStatusBarAndNavigationBarColor(activity: Activity, themeModel: Any) {
        // Status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getThemeModelColor(themeModel, StatusBarColor::class, true)?.let { navigationBarColor ->
                activity.window.statusBarColor = navigationBarColor
            }
        }

        // Navigation bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getThemeModelColor(themeModel, NavigationBarColor::class, true)?.let { navigationBarColor ->
                activity.window.statusBarColor = navigationBarColor
            }
        }
    }

    fun setLayoutInflater(activity: Activity, layoutInflaterFactory: LayoutInflater.Factory2) {
        activity.getLayoutInflater().setFactory2(layoutInflaterFactory);
    }

    fun getThemeModelColor(themeModel: Any, annotation: KClass<*>, isOptional: Boolean) : Int? {
        return ReflectionUtil.getValue(themeModel, annotation, isOptional)?.let { themeColor ->
            if (themeColor is String) Color.parseColor(themeColor) else themeColor as Int
        }
    }

    private fun assertWindowFlags(flags: Int, themeModel: Any) {
        if (hasBothWindowFlagAndThemeAnnotationSet(flags, FLAG_TRANSLUCENT_NAVIGATION, NavigationBarColor::class, themeModel)) {
            Log.w(javaClass.name, "Theme attribute @NavigationBarColor will have no effect with window flag FLAG_TRANSLUCENT_NAVIGATION set at same time" )
        }
        if (hasBothWindowFlagAndThemeAnnotationSet(flags, FLAG_TRANSLUCENT_STATUS, StatusBarColor::class, themeModel)) {
            Log.w(javaClass.name, "Theme attribute @NavigationBarColor will have no effect with window flag FLAG_TRANSLUCENT_STATUS set at same time")
        }
    }

    private fun hasBothWindowFlagAndThemeAnnotationSet(windowFlags: Int, windowFlag: Int, annotation: KClass<*>, themeModel: Any): Boolean {
        val hasWindowFlag = ((windowFlags and windowFlag) == windowFlag)
        val hasThemeModelColor = getThemeModelColor(themeModel, annotation, true)?.let { true } ?: false
        return hasWindowFlag and hasThemeModelColor
    }
}
