package themeux.annotation.flag

/**
 * Usage:
 * {@SystemUiFlags(SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)}
 *
 * @see android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
 * @see android.view.Window.getDecorView().setSystemUiVisibility()
 */
annotation class SystemUiFlags(val flags: Int)