package themeux.factory

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
import android.support.v4.content.res.ResourcesCompat
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
import themeux.annotation.color.AccentColor
import themeux.annotation.theme.Theme
import themeux.annotation.theme.ToolbarThemeOverlay
import themeux.util.ReflectionUtil

/**
 * Created by Manne Öhlund on 2018-02-26.
 * Copyright © 2018. All rights reserved.
 */

class DefaultThemeLayoutInflaterFactory(
        private val activity: Activity,
        private val themeModel: Any) : LayoutInflater.Factory2 {

    val theme: Int
    val toolbarThemeOverlay: Int
    val accentColor: Int?

    var themeWrapper: ContextThemeWrapper? = null
    var toolbarThemeOverlayWrapper: ContextThemeWrapper? = null

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
        if (themeWrapper == null) themeWrapper = ContextThemeWrapper(context, theme)
        if (toolbarThemeOverlayWrapper == null) toolbarThemeOverlayWrapper = ContextThemeWrapper(context, theme)

        if (viewName.startsWith("android.support")) {
            if (TextUtils.equals(viewName, Toolbar::class.java.name)) {
                val toolbar = Toolbar(toolbarThemeOverlayWrapper, attributeSet)
                toolbar.popupTheme = toolbarThemeOverlay
                return toolbar
            } else if (TextUtils.equals(viewName, AppBarLayout::class.java.name)) {
                return AppBarLayout(toolbarThemeOverlayWrapper, attributeSet)
            } else if (viewName.startsWith("android.support.v7.widget.AppCompat")) {
                if (TextUtils.equals(viewName, AppCompatRadioButton::class.java.name)) {
                    val radioButton = AppCompatRadioButton(themeWrapper, attributeSet)
                    CompoundButtonCompat.setButtonTintList(radioButton, tint)
                    return radioButton
                } else if (TextUtils.equals(viewName, AppCompatCheckBox::class.java.name)) {
                    val checkBox = AppCompatCheckBox(themeWrapper, attributeSet)
                    CompoundButtonCompat.setButtonTintList(checkBox, tint)
                    return checkBox
                } else if (TextUtils.equals(viewName, AppCompatEditText::class.java.name)) {
                    accentColor?.let {
                        val editText = AppCompatEditText(themeWrapper, attributeSet)
                            ViewCompat.setBackgroundTintList(editText, ColorStateList.valueOf(accentColor))
                            colorHandles(editText, accentColor)
                        return editText
                    }
                } else if (TextUtils.equals(viewName, AppCompatSeekBar::class.java.name)) {
                    accentColor?.let {
                        val seekBar = AppCompatSeekBar(themeWrapper, attributeSet)
                            seekBar.progressDrawable.setColorFilter(accentColor, PorterDuff.Mode.SRC_ATOP)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                seekBar.thumb.setColorFilter(accentColor, PorterDuff.Mode.SRC_ATOP)
                            }
                        return seekBar
                    }
                } else if (TextUtils.equals(viewName, AppCompatRatingBar::class.java.name)) {
                    accentColor?.let {
                        val ratingBar = AppCompatRatingBar(themeWrapper, attributeSet)
                        val stars = ratingBar.progressDrawable as LayerDrawable
                        stars.getDrawable(0).setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP)
                        stars.getDrawable(1).setColorFilter(accentColor, PorterDuff.Mode.SRC_ATOP)
                        stars.getDrawable(2).setColorFilter(accentColor, PorterDuff.Mode.SRC_ATOP)
                        //ratingBar.getProgressDrawable().setColorFilter(stylesModel.getAccentColor(), PorterDuff.Mode.SRC_OVER);
                        return ratingBar
                    }
                }
            } else if (viewName.startsWith("android.support.v4.widget")) {
                if (TextUtils.equals(viewName, ContentLoadingProgressBar::class.java.name)) {
                    accentColor?.let {
                        val progressBar = ContentLoadingProgressBar(themeWrapper, attributeSet)
                        progressBar.indeterminateDrawable.setColorFilter(accentColor, PorterDuff.Mode.SRC_ATOP)
                        return progressBar
                    }
                }
            } else if (viewName.startsWith("android.support.design.widget")) {
                if (TextUtils.equals(viewName, FloatingActionButton::class.java.name)) {
                    accentColor?.let {
                        val fab = FloatingActionButton(themeWrapper, attributeSet)
                        fab.setBackgroundTintList(ColorStateList.valueOf(accentColor));
                        return fab
                    }
                }
            }
        }

        // Try to let the Activity handle it (inflating fragments from XML)
        result = activity.onCreateView(viewName, themeWrapper, attributeSet)

        if (result == null && activity is AppCompatActivity) {
            // Get themed views from app compat
            result = activity.delegate.createView(parent, viewName, themeWrapper!!, attributeSet)
        }

        return result
    }

    // Helper
    val tint: ColorStateList
        get() = ColorStateList(
                arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
                intArrayOf(Color.DKGRAY, accentColor?:Color.GREEN)
        )

    val editTextTint: ColorStateList
        get() = ColorStateList(
                arrayOf(intArrayOf(-android.R.attr.state_focused), intArrayOf(android.R.attr.state_focused)),
                intArrayOf(Color.DKGRAY, accentColor?:Color.GREEN)
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

                    var handleDrawable: Drawable? = handleField.get(editor) as? Drawable

                    if (handleDrawable == null) {
                        val resField = TextView::class.java.getDeclaredField(resNames[i])
                        if (!resField.isAccessible) {
                            resField.isAccessible = true
                        }
                        val resId = resField.getInt(view)
                        handleDrawable = ResourcesCompat.getDrawable(view.resources, resId, null)
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
