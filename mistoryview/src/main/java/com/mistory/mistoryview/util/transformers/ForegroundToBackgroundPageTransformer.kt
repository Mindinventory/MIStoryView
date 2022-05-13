package com.mistory.mistoryview.util.transformers

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.min

class ForegroundToBackgroundPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, pos: Float) {
        val height = page.height.toFloat()
        val width = page.width.toFloat()
        val scale = min(if (pos > 0) 1f else abs(1f + pos), 1f)
        with(page) {
            scaleX = scale
            scaleY = scale
            pivotX = width * 0.5f
            pivotY = height * 0.5f
            translationX = if (pos > 0) width * pos else -width * pos * 0.25f
        }
    }
}