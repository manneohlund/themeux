# jsonthemer
Define theme in json file and dynamically load in to a ThemeModel and bind it to your views with DataBinding

# Gradle
#### Step 1. Add the JitPack repository to your build file
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
#### Step 2. Add the dependency
```groovy
dependencies {
    compile 'com.github.manneohlund:jsonthemer:1.0.0'
}
```

# Usage
### Json theme
Define themes in `.json` files and add it to `assets` folder

```json
{
  "theme": 1,
  "toolbarThemeOverlay": 0,
  "popupThemeOverlay": 1,
  "statusBarColor": "#388E3C",
  "toolbarColor": "#4CAF50",
  "accentColor": "#8BC34A",
  "navigationBarColor": "#388E3C",
  "backgroundColor": "#FF000000"
}
```

### Android Data Binding
Let the DataBinding in Android do the stitching.
Set the data model variable in your layout and bind model attributes in xml code ex: `android:background="@{themeModel.toolbarColor}"`.

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>
        <variable
            name="themeModel"
            type="jsonthemer.model.BaseThemeModel" />
    </data>
    
    <!-- Layouts -->
    <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="@{themeModel.toolbarColor}" />
</layout>
```

### Setup Json Theme in Activity
If you use or extend `BaseThemeModel` `JsonThemer` will set a `DefaultThemeLayoutInflaterFactory` that will handle the theme/style setting on all android.support components for `accentColor`.

```kotlin

override fun onCreate(savedInstanceState: Bundle?) {
    // Must be setup before onCreate to enable the json theme
    val theme: BaseThemeModel = JsonThemer.setup(this, currentTheme)
    
    // Call super after the JsonThemer setup
    super.onCreate(savedInstanceState)
    
    // Setup data binding and set the theme
    var binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    binding.themeModel = theme
}
```

# Advanced usage

### ThemeModel
Define a `ThemeModel` or use the default `BaseThemeModel`

```kotlin
class BaseThemeModel : ThemeModel {
    val LIGHT = 0
    val DARK = 1

    override var theme: Int = LIGHT
    override var statusBarColor: String = "#FFFF5722"
    override var navigationBarColor: String = "#FFFF5722"

    // Theme overlays
    private var toolbarThemeOverlay: Int = LIGHT
    private var popupThemeOverlay: Int = LIGHT

    // Other
    private var toolbarColor: String = "#FFFF5722"
    private var accentColor: String = "#FFFF5722"
    private var windowBackgroundColor: String = "#FF000000"

    override fun getModelTheme(): Int {
        return when (theme) {
            DARK -> return android.support.v7.appcompat.R.style.Theme_AppCompat_NoActionBar
            else -> android.support.v7.appcompat.R.style.Theme_AppCompat_Light_NoActionBar
        }
    }

    fun getToolbarThemeOverlay(): Int {
        return when (toolbarThemeOverlay) {
            DARK -> return android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_Dark_ActionBar
            else -> android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_ActionBar
        }
    }

    fun getPopupThemeOverlay(): Int {
        return when (popupThemeOverlay) {
            DARK -> return android.support.v7.appcompat.R.style.Theme_AppCompat
            else -> android.support.v7.appcompat.R.style.Theme_AppCompat_Light
        }
    }

    override fun getStatusBarColor(): Int {
        return Color.parseColor(statusBarColor)
    }
    fun getStatusBarColorDrawable(): ColorDrawable {
        return ColorDrawable(getStatusBarColor())
    }
    override fun getToolbarColor(): Int {
        return Color.parseColor(toolbarColor)
    }
    fun getAccentColor(): Int {
        return Color.parseColor(accentColor)
    }
    override fun getNavigationBarColor(): Int {
        return Color.parseColor(navigationBarColor)
    }
    fun getWindowBackgroundColor(): Int {
        return Color.parseColor(windowBackgroundColor)
    }
}
```
