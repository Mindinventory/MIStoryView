package com.mistory.mistoryview.util.transformers

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ZoomInTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, pos: Float) {
        val scale = if (pos < 0) pos + 1f else Math.abs(1f - pos)
        with(page) {
            scaleX = scale
            scaleY = scale
            pivotX = page.width * 0.5f
            pivotY = page.height * 0.5f
            alpha = if (pos < -1f || pos > 1f) 0f else 1f - (scale - 1f)
        }
    }

    companion object {
        const val MAX_ROTATION = 90.0f
    }
}