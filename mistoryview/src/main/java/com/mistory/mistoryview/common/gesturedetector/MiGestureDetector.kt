package com.mistory.mistoryview.common.gesturedetector

import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent

internal class MiGestureDetector(private val mGestureListener: GestureListener) :
    SimpleOnGestureListener() {

    /**
     * On touch down but haven't moved or up yet.
     * Which will help to pause/resume the progress
     * when user long press the view
     */
    override fun onShowPress(e: MotionEvent?) {
        mGestureListener.onLongPressOccurred(e)
        super.onShowPress(e)
    }

    /**
     * Triggered when tap up event occurs
     */
    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        mGestureListener.onSingleTapOccurred(e)
        return super.onSingleTapUp(e)
    }
}

interface GestureListener {
    fun onLongPressOccurred(e: MotionEvent?)
    fun onSingleTapOccurred(e: MotionEvent?)
}