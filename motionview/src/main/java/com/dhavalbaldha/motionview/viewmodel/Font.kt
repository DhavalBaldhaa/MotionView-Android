package com.dhavalbaldha.motionview.viewmodel

class Font {
    /**
     * color value (ex: 0xFF00FF)
     */
    var color = 0

    /**
     * name of the font
     */
    var typeface: String? = null

    /**
     * size of the font, relative to parent
     */
    var size = 0f
    fun increaseSize(diff: Float) {
        size += diff
    }

    fun decreaseSize(diff: Float) {
        if (size - diff >= Limits.MIN_FONT_SIZE) size -= diff
    }

    private interface Limits {
        companion object {
            const val MIN_FONT_SIZE = 0.01f
        }
    }
}