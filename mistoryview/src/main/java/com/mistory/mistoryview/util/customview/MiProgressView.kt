package com.mistory.mistoryview.util.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.example.mistoryview.R

class MiProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var frontProgressView: View? = null
    private var maxProgressView: View? = null
    private var miScaleAnimation: MiScaleAnimation? = null
    private var duration = DEFAULT_PROGRESS_DURATION
    private var callback: Callback? = null
    private var isStarted = false

    init {
        LayoutInflater.from(context).inflate(R.layout.pausable_progress, this)
        frontProgressView = findViewById(R.id.front_progress)
        maxProgressView = findViewById(R.id.max_progress)
    }

    fun setDuration(duration: Long) {
        this.duration = duration
        if (miScaleAnimation != null){
            miScaleAnimation = null
            startProgress()
        }
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun setMax() {
        finishProgress(true)
    }

    fun setMin() {
        finishProgress(false)
    }

    fun setMinWithoutCallback() {
        maxProgressView!!.setBackgroundResource(R.color.progress_secondary)
        maxProgressView!!.visibility = View.VISIBLE
        if (miScaleAnimation != null) {
            miScaleAnimation!!.setAnimationListener(null)
            miScaleAnimation!!.cancel()
        }
    }

    fun setMaxWithoutCallback() {
        maxProgressView!!.setBackgroundResource(R.color.progress_max_active)
        maxProgressView!!.visibility = View.VISIBLE
        if (miScaleAnimation != null) {
            miScaleAnimation!!.setAnimationListener(null)
            miScaleAnimation!!.cancel()
        }
    }

    private fun finishProgress(isMax: Boolean) {
        if (isMax) maxProgressView!!.setBackgroundResource(R.color.progress_max_active)
        maxProgressView!!.visibility = if (isMax) View.VISIBLE else View.GONE
        if (miScaleAnimation != null) {
            miScaleAnimation!!.setAnimationListener(null)
            miScaleAnimation!!.cancel()
            if (callback != null) {
                callback!!.onFinishProgress()
            }
        }
    }

    fun startProgress() {
        maxProgressView!!.visibility = View.GONE
        if (duration <= 0) duration = DEFAULT_PROGRESS_DURATION
        miScaleAnimation =
            MiScaleAnimation(
                0f,
                1f,
                1f,
                1f,
                Animation.ABSOLUTE,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f
            )
        miScaleAnimation!!.duration = duration
        miScaleAnimation!!.interpolator = LinearInterpolator()
        miScaleAnimation!!.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                if (isStarted) {
                    return
                }
                isStarted = true
                frontProgressView!!.visibility = View.VISIBLE
                if (callback != null) callback!!.onStartProgress()
            }

            override fun onAnimationEnd(animation: Animation) {
                isStarted = false
                if (callback != null) callback!!.onFinishProgress()
            }

            override fun onAnimationRepeat(animation: Animation) {
                //NO-OP
            }
        })
        miScaleAnimation!!.fillAfter = true
        frontProgressView!!.startAnimation(miScaleAnimation)
    }

    fun pauseProgress() {
        if (miScaleAnimation != null) {
            miScaleAnimation!!.pause()
        }
    }

    fun resumeProgress() {
        if (miScaleAnimation != null) {
            miScaleAnimation!!.resume()
        }
    }

    fun clear() {
        if (miScaleAnimation != null) {
            miScaleAnimation!!.setAnimationListener(null)
            miScaleAnimation!!.cancel()
            miScaleAnimation = null
        }
    }

    interface Callback {
        fun onStartProgress()
        fun onFinishProgress()
    }

    companion object {
        private const val DEFAULT_PROGRESS_DURATION = 4000L
    }
}