package com.mistory.mistoryview.util.transformers

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class RotateDownPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val width = page.width.toFloat()
        val rotation = ROTATION * position
        with(page) {
            pivotX = width * 0.5f
            pivotY = 0f
            translationX = 0f
            this.rotation = rotation
        }
    }

    companion object {
        private const val ROTATION = -15f
    }
}