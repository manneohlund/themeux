package jsonthemer

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import jsonthemer.factory.DefaultThemeLayoutInflaterFactory
import jsonthemer.model.BaseThemeModel
import jsonthemer.model.ThemeModel
import jsonthemer.util.AssetsUtils

/**
 * Created by Manne Öhlund on 2018-02-27.
 * Copyright © 2018. All rights reserved.
 */

class JsonThemer {

    companion object {
        inline fun <reified T: ThemeModel> setup(activity: AppCompatActivity, assetsFileName: String) : T {
            var themeModel: T = AssetsUtils.loadStyleAssets(activity, assetsFileName)
            activity.setTheme(themeModel.getModelTheme())

            setStatusBarAndNavigationBarColor(activity, themeModel)

            if (themeModel is BaseThemeModel) {
                setLayoutInflater(activity, DefaultThemeLayoutInflaterFactory(activity, themeModel as BaseThemeModel))
            }

            return themeModel
        }

        fun setStatusBarAndNavigationBarColor(activity: AppCompatActivity, theme: ThemeModel) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.window.statusBarColor = theme.getStatusBarColor()
                activity.window.navigationBarColor = theme.getNavigationBarColor()
            }
        }

        fun setLayoutInflater(activity: AppCompatActivity, layoutInflaterFactory: LayoutInflater.Factory2) {
            activity.getLayoutInflater().setFactory2(layoutInflaterFactory);
        }
    }
}
