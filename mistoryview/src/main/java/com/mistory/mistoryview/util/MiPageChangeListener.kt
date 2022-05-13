package com.mistory.mistoryview.util

import android.os.Handler
import android.os.Looper
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.*

abstract class MiPageChangeListener : ViewPager2.OnPageChangeCallback() {

    private var pageIndexBeforeDragging = 0
    private var currentPageIndex = 0
    private var lastTime = DEBOUNCE_TIMES + 1L

    override fun onPageScrollStateChanged(state: Int) {
        when (state) {
            SCROLL_STATE_IDLE -> {
                val now = System.currentTimeMillis()
                if (now - lastTime < DEBOUNCE_TIMES) {
                    return
                }
                lastTime = now
                Handler(Looper.getMainLooper()).postDelayed({
                    if (pageIndexBeforeDragging == currentPageIndex) {
                        onPageScrollCanceled()
                    }
                }, 300L)
            }
            SCROLL_STATE_DRAGGING -> {
                pageIndexBeforeDragging = currentPageIndex
            }
            else -> {}
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        currentPageIndex = position
    }

    abstract fun onPageScrollCanceled()

    companion object {
        private const val DEBOUNCE_TIMES = 500L
    }
}
