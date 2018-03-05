package themeux.annotation.flag

/**
 * Created by Manne Öhlund on 2018-02-27.
 * Copyright © 2018. All rights reserved.
 */

/**
 * Usage Kotlin:
 *
 * {@WindowFlags(FLAG_LAYOUT_IN_SCREEN or FLAG_LAYOUT_NO_LIMITS)}
 * {@WindowFlags(FLAG_LAYOUT_IN_SCREEN or FLAG_LAYOUT_NO_LIMITS or FLAG_TRANSLUCENT_NAVIGATION)}
 *
 * Usage Java:
 * {@WindowFlags(FLAG_LAYOUT_IN_SCREEN | FLAG_LAYOUT_NO_LIMITS)}
 * {@WindowFlags(FLAG_LAYOUT_IN_SCREEN | FLAG_LAYOUT_NO_LIMITS | FLAG_TRANSLUCENT_NAVIGATION)}
 *
 * @see android.view.WindowManager.LayoutParams
 * @see android.view.Window.addFlags()
 */
annotation class WindowFlags(val flags: Int)