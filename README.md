<h1 align="center">MotionView-Android</h1>
<p align="center">
  <a href="https://jitpack.io/#DhavalBaldhaa/MotionView-Androidr"> <img src="https://jitpack.io/v/DhavalBaldhaa/MotionView-Android/month.svg" /></a>
  <a href="https://jitpack.io/#DhavalBaldhaa/MotionView-Android"> <img src="https://jitpack.io/v/DhavalBaldhaa/MotionView-Android.svg" /></a>
</p>

MotionView is android library which will help you to create Snapchat-like text stickers and image stickers. After adding you can manipulate then using gesture

# Installation
Step 1. Add the JitPack repository to your build file
```
allprojects {
    repositories {
	...
	maven { url 'https://jitpack.io' }
}
```
Step 2. Add the dependency
```
dependencies {
    implementation 'com.github.DhavalBaldhaa:MotionView-Android:Tag'
}
```

Step 3. Add MotionView in your xml file as container
```
 <com.dhavalbaldha.motionview.MotionView
        android:id="@+id/motionView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

# Usage
**Create Font Provider**
```
fontProvider = FontProvider(resources, fonts())
```

**Add Text Sticker**
```
private fun createTextLayer(): TextLayer {
    val textLayer = TextLayer()
    val font = Font()
    font.color = TextLayer.Limits.INITIAL_FONT_COLOR
    font.size = TextLayer.Limits.INITIAL_FONT_SIZE
    font.typeface = fontProvider?.defaultFontName
    textLayer.font = font
    textLayer.text = getString(R.string.app_name)
    return textLayer
}

val textLayer = createTextLayer()
val textEntity = TextEntity(textLayer, width, height, fontProvider)
motionView.addEntityAndPosition(textEntity)

// move text sticker up so that its not hidden under keyboard
val center = textEntity.absoluteCenter()
center.y = center.y * 0.5f
textEntity.moveCenterTo(center)

// redraw
motionView.invalidate()
startTextEntityEditing()
```

* Bug reports and pull requests are welcome.
* Make sure you use [square/java-code-styles](https://github.com/square/java-code-styles) to format your code.
