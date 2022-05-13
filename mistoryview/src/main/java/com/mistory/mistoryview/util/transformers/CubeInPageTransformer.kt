package com.mistory.mistoryview.util.transformers

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class CubeInPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        // Rotate the fragment on the left or right edge
        with(page) {
            pivotX = if (position > 0)
                0.toFloat()
            else
                width.toFloat()
            pivotY = 0f
            rotationY = -90f * position
        }
    }
}