package jsonthemer.factory

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.support.design.widget.AppBarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.support.v4.widget.CompoundButtonCompat
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.text.TextUtils
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import jsonthemer.annotation.AccentColor
import jsonthemer.annotation.Theme
import jsonthemer.annotation.ToolbarThemeOverlay
import jsonthemer.util.ReflectionUtil

/**
 * Created by Manne Öhlund on 2018-02-26.
 * Copyright © 2018. All rights reserved.
 */

class DefaultThemeLayoutInflaterFactory(
        private val activity: Activity,
        private val themeModel: Any) : LayoutInflater.Factory2 {

    val theme: Int
    val toolbarThemeOverlay: Int
    val accentColor: Int

    init {
        theme = ReflectionUtil.getValue(themeModel, Theme::class) as Int
        toolbarThemeOverlay = ReflectionUtil.getValue(themeModel, ToolbarThemeOverlay::class) as Int
        accentColor = ReflectionUtil.getValue(themeModel, AccentColor::class).let {
            if (it is String) Color.parseColor(it) else it as Int
        }
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        return inflateView(parent, name, context, attrs)
    }

    override fun onCreateView(viewName: String, context: Context, attributeSet: AttributeSet): View? {
        return inflateView(null, viewName, context, attributeSet)
    }

    @SuppressLint("RestrictedApi")
    private fun inflateView(parent: View?, viewName: String, context: Context, attributeSet: AttributeSet): View? {
        var result: View?
        var wrapper = context

        if (viewName.startsWith("android.support")) {
            wrapper = ContextThemeWrapper(context, toolbarThemeOverlay)

            if (TextUtils.equals(viewName, Toolbar::class.java.name)) {
                val toolbar = Toolbar(wrapper, attributeSet)
                toolbar.popupTheme = toolbarThemeOverlay
                return toolbar
            } else if (TextUtils.equals(viewName, AppBarLayout::class.java.name)) {
                return AppBarLayout(wrapper, attributeSet)
            } else if (viewName.startsWith("android.support.v7.widget.AppCompat")) {
                wrapper = ContextThemeWrapper(context, theme)
                if (TextUtils.equals(viewName, AppCompatRadioButton::class.java.name)) {
                    val radioButton = AppCompatRadioButton(wrapper, attributeSet)
                    CompoundButtonCompat.setButtonTintList(radioButton, tint)
                    return radioButton
                } else if (TextUtils.equals(viewName, AppCompatCheckBox::class.java.name)) {
                    val checkBox = AppCompatCheckBox(wrapper, attributeSet)
                    CompoundButtonCompat.setButtonTintList(checkBox, tint)
                    return checkBox
                } else if (TextUtils.equals(viewName, AppCompatEditText::class.java.name)) {
                    val editText = AppCompatEditText(wrapper, attributeSet)
                    ViewCompat.setBackgroundTintList(editText, ColorStateList.valueOf(accentColor))
                    colorHandles(editText, accentColor)
                    return editText
                } else if (TextUtils.equals(viewName, AppCompatSeekBar::class.java.name)) {
                    val seekBar = AppCompatSeekBar(wrapper, attributeSet)
                    seekBar.progressDrawable.setColorFilter(accentColor, PorterDuff.Mode.SRC_ATOP)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        seekBar.thumb.setColorFilter(accentColor, PorterDuff.Mode.SRC_ATOP)
                    }
                    return seekBar
                } else if (TextUtils.equals(viewName, AppCompatRatingBar::class.java.name)) {
                    val ratingBar = AppCompatRatingBar(wrapper, attributeSet)
                    val stars = ratingBar.progressDrawable as LayerDrawable
                    stars.getDrawable(0).setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP)
                    stars.getDrawable(1).setColorFilter(accentColor, PorterDuff.Mode.SRC_ATOP)
                    stars.getDrawable(2).setColorFilter(accentColor, PorterDuff.Mode.SRC_ATOP)
                    //ratingBar.getProgressDrawable().setColorFilter(stylesModel.getAccentColor(), PorterDuff.Mode.SRC_OVER);
                    return ratingBar
                }
            } else if (viewName.startsWith("android.support.v4.widget")) {
                wrapper = ContextThemeWrapper(context, theme)
                if (TextUtils.equals(viewName, ContentLoadingProgressBar::class.java.name)) {
                    val progressBar = ContentLoadingProgressBar(wrapper, attributeSet)
                    progressBar.indeterminateDrawable.setColorFilter(accentColor, PorterDuff.Mode.SRC_ATOP)
                    return progressBar
                }
            } else if (viewName.startsWith("android.support.design.widget")) {
                wrapper = ContextThemeWrapper(context, theme)
                if (TextUtils.equals(viewName, FloatingActionButton::class.java.name)) {
                    val fab = FloatingActionButton(wrapper, attributeSet)
                    fab.setBackgroundTintList(ColorStateList.valueOf(accentColor));
                    return fab
                }
            }
        }

        // Try to let the Activity handle it (inflating fragments from XML)
        result = activity.onCreateView(viewName, wrapper, attributeSet)

        if (result == null && activity is AppCompatActivity) {
            // Get themed views from app compat
            result = activity.delegate.createView(parent, viewName, wrapper, attributeSet)
        }

        return result
    }

    // Helper
    val tint: ColorStateList
        get() = ColorStateList(
                arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
                intArrayOf(Color.DKGRAY, accentColor)
        )

    val editTextTint: ColorStateList
        get() = ColorStateList(
                arrayOf(intArrayOf(-android.R.attr.state_focused), intArrayOf(android.R.attr.state_focused)),
                intArrayOf(Color.DKGRAY, accentColor)
        )
    companion object {

        fun colorHandles(view: TextView, color: Int) {
            try {
                val editorField = TextView::class.java.getDeclaredField("mEditor")
                if (!editorField.isAccessible) {
                    editorField.isAccessible = true
                }

                val editor = editorField.get(view)
                val editorClass = editor.javaClass

                val handleNames = arrayOf("mSelectHandleLeft", "mSelectHandleRight", "mSelectHandleCenter")
                val resNames = arrayOf("mTextSelectHandleLeftRes", "mTextSelectHandleRightRes", "mTextSelectHandleRes")

                for (i in handleNames.indices) {
                    val handleField = editorClass.getDeclaredField(handleNames[i])
                    if (!handleField.isAccessible) {
                        handleField.isAccessible = true
                    }

                    var handleDrawable: Drawable? = handleField.get(editor) as Drawable

                    if (handleDrawable == null) {
                        val resField = TextView::class.java.getDeclaredField(resNames[i])
                        if (!resField.isAccessible) {
                            resField.isAccessible = true
                        }
                        val resId = resField.getInt(view)
                        handleDrawable = view.resources.getDrawable(resId)
                    }

                    if (handleDrawable != null) {
                        val drawable = handleDrawable.mutate()
                        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                        handleField.set(editor, drawable)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
