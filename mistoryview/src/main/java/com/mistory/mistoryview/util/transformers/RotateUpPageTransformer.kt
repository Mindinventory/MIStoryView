package com.mistory.mistoryview.util.transformers

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class RotateUpPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, pos: Float) {
        val width = page.width.toFloat()
        val height = page.height.toFloat()
        val rotation = ROTATION * pos * -1.25f
        with(page) {
            pivotX = width * 0.5f
            pivotY = height
            this.rotation = rotation
        }
    }

    companion object {
        private const val ROTATION = -15f
    }
}