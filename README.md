
# Themeux
![Thermux](https://raw.githubusercontent.com/manneohlund/themeux/master/sample/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png)

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
    compile 'com.github.manneohlund:themeux:3.1.1'
}
```

# Basic Usage
### Theme class

Create a class with any name and assign variables with key annotations to map preferred ui component to style.<br/>
You can pretty much build a ThemeModel with any attribute naming and set proper target annotation.<br/>
Colors can either be `hex` or as `integer`.<br/>
You can specify your own theme in styles and reference it via the package `R.style`.
Some annotations below are mandatory!<br/>

```kotlin
class ThemeModel {
    @Theme
    val theme = android.support.v7.appcompat.R.style.Theme_AppCompat_NoActionBar

    @ToolbarThemeOverlay
    val toolbarThemeOverlay = android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_Dark_ActionBar

    @ToolbarPopupThemeOverlay
    val popupThemeOverlay = android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_Dark

    @AccentColor
    val accentColor = Color.parseColor("#8BC34A")

    @TaskDescriptionColor
    @StatusBarColor
    @PrimaryColor
    val primaryColor = Color.parseColor("#4CAF50")

    @NavigationBarColor
    @PrimaryDarkColor
    val primaryDarkColor = Color.parseColor("#C8388E3C")
}
```

#### Or for example

```kotlin
class ThemeModel {
    @Theme
    var theme = android.support.v7.appcompat.R.style.Theme_AppCompat_NoActionBar
    @StatusBar
    var statusBarColor = "#FFFF5722" // String or int color
    @NavigationBar
    var navigationBarColor = "#FFFF5722"
    @ToolbarBar
    var toolbarColor: String = "#FFFF5722"
    @AccentColor
    var accentColor: String = "#FFFF5722"
    
    // Other colors
    @PrimaryColor
    var primaryColor: String = "#FFFF5722"
}
```

### Setup Theme in Activity

`Themeux` will set a `DefaultThemeLayoutInflaterFactory` that will handle the theme/style setting on all android.support components for `accentColor`.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    // Must be setup before onCreate to enable the json theme
    val theme = Themeux.setup(this, ThemeModel::class)
    
    // Call super after the JsonThemer setup
    super.onCreate(savedInstanceState)
    
    ...
}
```

# Android Data Binding + ThemeModel
Let the DataBinding in Android do the stitching.
Set the data model variable in your layout and bind model attributes in xml code ex: `android:background="@{themeModel.primaryColor}"`.

#### XML

```xml
// R.layout.activity_main
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="themeModel"
            type="com.somepackage.model.ThemeModel" />
    </data>
    
    <!-- Layouts -->
    <android.support.v7.widget.SomeView
	    ...
        android:background="@{themeModel.primaryColor}" />
</layout>
```

#### Activity

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    // Must be setup before onCreate to enable the json theme
    val theme = Themeux.setup(this, ThemeModel::class)
    
    // Call super after the JsonThemer setup
    super.onCreate(savedInstanceState)
    
    // Setup data binding and set the theme and make custom changes to the layout
    var binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    binding.themeModel = theme
    
    // Set ActionBar from bindings  
    setSupportActionBar(binding.toolbar) 
}
```

# Advanced Usage
#### Json theme

Define themes in `.json` files and add it to `assets` folder, load them with your ThemeModel to override variables with the `json` theme variables.

```json
// blue_theme.json
{  
  "theme": 0,  
  "toolbarThemeOverlay": 1,  
  "popupThemeOverlay": 0,  
  "statusBarColor": "#303F9F",  
  "toolbarColor": "#C83F51B5",  
  "accentColor": "#448AFF",  
  "navigationBarColor": "#C8303F9F",  
  "backgroundColor": "#FFFFFFFF"  
}
```

#### ThemeModel
Note that annotations can be put on functions also! See `@Theme` `@ToolbarThemeOverlay`

```kotlin
class ThemeModel {  
  val LIGHT = 0  
  val DARK = 1  
  
  var theme = LIGHT
  @StatusBar  
  var statusBarColor = "#FFFF5722"  
  @NavigationBar  
  var navigationBarColor = "#FFFF5722"  
  
  @ToolbarBar  
  val toolbarColor = "#FFFF5722"  
  @AccentColor  
  val accentColor = "#FFFF5722"
  
  // Theme overlays  
  private var toolbarThemeOverlay: Int = LIGHT  
  private var popupThemeOverlay: Int = LIGHT
  
  // Other
  var windowBackgroundColor: String = "#FF000000"  
  
  @Theme  
  fun getModelTheme(): Int {  
        return when (theme) {  
            DARK -> return android.support.v7.appcompat.R.style.Theme_AppCompat_NoActionBar  
            else -> android.support.v7.appcompat.R.style.Theme_AppCompat_Light_NoActionBar  
        }  
    }  
  
  @ToolbarThemeOverlay  
  fun getToolbarThemeOverlay(): Int {  
        return when (toolbarThemeOverlay) {  
            DARK -> return android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_Dark_ActionBar  
            else -> android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_ActionBar  
        }  
    }
}
```

#### Activity
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
	// Load json theme and override target model
    val theme: ThemeModel = let {  
        try {
            Themeux.setup(this, "blue_theme.json")  
        } catch (e: Exception) {  
            Log.e(MainActivity::class.simpleName, "Error: AssetFileNotFound, " \+ e.message)  
  
            // Fallback on base model  
            ThemeModel()  
        }  
    } 
    
    // Call super after the JsonThemer setup
    super.onCreate(savedInstanceState) 
    
    // Setup data binding and set the theme and make custom changes to the layout
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)  
    binding.themeModel = theme  
  
    // Set ActionBar from bindings  
    setSupportActionBar(binding.toolbar)  
}
```
