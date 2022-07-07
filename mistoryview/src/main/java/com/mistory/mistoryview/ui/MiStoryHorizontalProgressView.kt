package com.mistory.mistoryview.ui

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.mistoryview.R
import com.mistory.mistoryview.common.INITIAL_STORY_INDEX
import com.mistory.mistoryview.common.extension.dpToPx
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity

class MiStoryHorizontalProgressView : View {

    private val TAG = javaClass.simpleName
    private var mProgressHeight = 0
    private var mGapBetweenProgressBars = 0
    private var mProgressBarPrimaryColor = 0
    private var mProgressBarSecondaryColor = 0
    private var mSingleStoryDisplayTime: Long = 0
    private var resource: Resources? = null

    private var mScreenWidth = 0
    private var mProgressPaint: Paint? = null

    private var mProgressbarRectF: RectF? = null

    private var topValue = 0
    private var bottomValue = 0

    private var mProgressbarCount = 0
    private lateinit var mProgressBarRightEdge: IntArray

    private var mSingleProgressBarWidth = 0
    private var mMiStoryPlayerListener: MiStoryPlayerListener? = null

    private var mProgressAnimators: ValueAnimator? = null
    private var isCancelled = false

    private var currentIndex: Int = 0

    companion object {
        const val MI_PROGRESS_BAR_HEIGHT = 2
        const val MI_GAP_BETWEEN_PROGRESS_BARS = 2
        const val MI_SINGLE_STORY_DISPLAY_TIME = 2000
        const val MI_PROGRESS_PRIMARY_COLOR = "#ebebe6"
        const val MI_PROGRESS_SECONDARY_COLOR = "#696868"
    }

    constructor(context: Context) : super(context) {
        resource = context.resources
        setDefaultValue()
        initView()
        prepareValues()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        resource = context.resources
        initView()
        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.MiStoryHorizontalProgressView, 0, 0
        )

        try {
            resource?.let {
                mProgressHeight = typedArray.getDimension(
                    R.styleable.MiStoryHorizontalProgressView_miProgressBarHeight,
                    MI_PROGRESS_BAR_HEIGHT.toFloat()
                ).toInt().dpToPx(it)

                mGapBetweenProgressBars = typedArray.getDimension(
                    R.styleable.MiStoryHorizontalProgressView_miGapBetweenProgressBar,
                    MI_GAP_BETWEEN_PROGRESS_BARS.toFloat()
                ).toInt().dpToPx(it)

                mProgressBarPrimaryColor = typedArray.getColor(
                    R.styleable.MiStoryHorizontalProgressView_miProgressBarPrimaryColor,
                    Color.parseColor(MI_PROGRESS_PRIMARY_COLOR)
                )

                mProgressBarSecondaryColor = typedArray.getColor(
                    R.styleable.MiStoryHorizontalProgressView_miProgressBarSecondaryColor,
                    Color.parseColor(MI_PROGRESS_SECONDARY_COLOR)
                )

                mSingleStoryDisplayTime = typedArray.getInt(
                    R.styleable.MiStoryHorizontalProgressView_miSingleStoryDisplayTime,
                    MI_SINGLE_STORY_DISPLAY_TIME
                ).toLong()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
        prepareValues()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 0 until mProgressbarCount) {
            val left =
                (mGapBetweenProgressBars + mSingleProgressBarWidth) * i + mGapBetweenProgressBars
            var right = (i + 1) * (mGapBetweenProgressBars + mSingleProgressBarWidth)
            mProgressPaint?.let { progressPaint ->
                progressPaint.color = mProgressBarSecondaryColor
                mProgressbarRectF?.let { rectF ->
                    rectF[left.toFloat(), topValue.toFloat(), right.toFloat()] =
                        bottomValue.toFloat()

                    canvas?.drawRoundRect(
                        rectF, mProgressHeight.toFloat(), mProgressHeight.toFloat(), progressPaint
                    )

                    right = mProgressBarRightEdge[i]

                    if (right > 0) {
                        progressPaint.color = mProgressBarPrimaryColor
                        rectF[left.toFloat(), topValue.toFloat(), right.toFloat()] =
                            bottomValue.toFloat()

                        canvas?.drawRoundRect(
                            rectF,
                            mProgressHeight.toFloat(),
                            mProgressHeight.toFloat(),
                            progressPaint
                        )
                    }
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = mScreenWidth - paddingStart + paddingEnd
        val height = paddingTop + paddingBottom + 2 * mGapBetweenProgressBars + mProgressHeight

        val w = resolveSizeAndState(width, widthMeasureSpec, 0)
        val h = resolveSizeAndState(height, heightMeasureSpec, 0)
        setMeasuredDimension(w, h)
    }

    private fun setDefaultValue() {
        resource?.let {
            mProgressHeight = MI_PROGRESS_BAR_HEIGHT.dpToPx(it)
            mGapBetweenProgressBars = MI_GAP_BETWEEN_PROGRESS_BARS.dpToPx(it)
            mProgressBarPrimaryColor = Color.parseColor(MI_PROGRESS_PRIMARY_COLOR)
            mProgressBarSecondaryColor = Color.parseColor(MI_PROGRESS_SECONDARY_COLOR)
            mSingleStoryDisplayTime = MI_SINGLE_STORY_DISPLAY_TIME.toLong()
        }
    }

    private fun initView() {
        mScreenWidth = resources.displayMetrics.widthPixels
        mProgressPaint = Paint()
        mProgressPaint?.let { mProgressPaint ->
            mProgressPaint.isAntiAlias = true
        }
    }

    private fun prepareValues() {
        topValue = mGapBetweenProgressBars
        bottomValue = mGapBetweenProgressBars + mProgressHeight
        mProgressPaint?.apply {
            color = mProgressBarSecondaryColor
            style = Paint.Style.FILL
            strokeCap = Paint.Cap.ROUND
        }
        mProgressbarRectF = RectF(0F, topValue.toFloat(), 0F, bottomValue.toFloat())
    }

    /**
     * Pause progress when user long tap on any story
     * or pause story when current story is not
     * full visible to user i.e in transition
     * of viewpager.
     */
    fun pause() {
        if (mProgressAnimators?.isRunning == true) {
            mProgressAnimators?.pause()
        }
    }

    /**
     * Resume progress when user release the touch of story view
     */
    fun resume() {
        if (mProgressAnimators?.isPaused == true) {
            mProgressAnimators?.resume()
        }
    }

    /**
     * Cancel animation when screen containing
     * this view is destroyed i.e Activity/Fragment.
     */
    fun cancelAnimation() {
        if (mProgressAnimators != null) {
            mProgressAnimators?.cancel()
            isCancelled = true
        }
    }

    /**
     * Set progress bar count for particular sub story view.
     */
    private fun setMiStoryProgressBarCount(count: Int) {
        require(count >= 1) { "Count cannot be less than 1" }
        mProgressbarCount = count
        mProgressBarRightEdge = IntArray(mProgressbarCount)
        calculateWidthOfEachProgressBar()
        invalidate()
    }

    /**
     * Set duration for sub story view
     */
    fun setSingleStoryDisplayTime(time: Long?) {
        if (time != null && time > 0L) {
            mSingleStoryDisplayTime = time
            invalidate()
        }
    }

    private fun setProgressBarPrimaryColor(colorPrimary: Int) {
        mProgressBarPrimaryColor = colorPrimary
        invalidate()
    }

    private fun setProgressBarSecondaryColor(colorSecondary: Int) {
        mProgressBarSecondaryColor = colorSecondary
        invalidate()
    }

    private fun setProgressBarHeight(progressBarHeight: Int) {
        this.mProgressHeight = progressBarHeight
        invalidate()
    }

    private fun setGapBetweenProgressBars(mGapBetweenProgressBars: Int) {
        this.mGapBetweenProgressBars = mGapBetweenProgressBars
        invalidate()
    }

    fun consumeAttrib(hashOfAttributeSet: HashMap<String, Any>, storyCount: Int) {
        setGapBetweenProgressBars(hashOfAttributeSet[MiStoryDisplayActivity.MI_FULLSCREEN_GAP_BETWEEN_PROGRESSBAR] as Int)
        setProgressBarHeight(hashOfAttributeSet[MiStoryDisplayActivity.MI_FULLSCREEN_PROGRESSBAR_HEIGHT] as Int)
        setProgressBarPrimaryColor(hashOfAttributeSet[MiStoryDisplayActivity.MI_FULLSCREEN_PROGRESSBAR_PRIMARY_COLOR] as Int)
        setProgressBarSecondaryColor(hashOfAttributeSet[MiStoryDisplayActivity.MI_FULLSCREEN_PROGRESSBAR_SECONDARY_COLOR] as Int)
        setMiStoryProgressBarCount(storyCount)
    }

    private fun calculateWidthOfEachProgressBar() {
        mSingleProgressBarWidth =
            (mScreenWidth - (mProgressbarCount + 1) * mGapBetweenProgressBars) / mProgressbarCount
    }

    /**
     * Start animation of progress view with
     * starting index. Also move to next story
     * point automatically when animations ends.
     */
    fun startAnimating(index: Int) {
        // Hold the current index of story point.
        currentIndex = index

        if (index >= mProgressbarCount) {
            if (mMiStoryPlayerListener != null) {
                mMiStoryPlayerListener?.onFinishedPlaying(isAtLastIndex = true)
                return
            }
        }

        mProgressAnimators = ValueAnimator.ofInt(0, mSingleProgressBarWidth)
        (mProgressAnimators as ValueAnimator).apply {
            interpolator = LinearInterpolator()
            duration = mSingleStoryDisplayTime
            addUpdateListener { valueAnimator ->
                mMiStoryPlayerListener?.getCurrentTime(
                    valueAnimator.currentPlayTime,
                    valueAnimator.duration
                )
                val value = valueAnimator.animatedValue as Int

                mProgressBarRightEdge[index] =
                    (index + 1) * mGapBetweenProgressBars + index * mSingleProgressBarWidth + value
                invalidate()
            }

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    mMiStoryPlayerListener?.onStoryFinished(index)

                    if (!isCancelled)
                        postDelayed({
                            startAnimating(index + 1)
                        }, 200)
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isCancelled = true
                }

                override fun onAnimationRepeat(animation: Animator?) {}
            })
            start()
        }
        mMiStoryPlayerListener?.onStartedPlaying(index)
    }

    /**
     * Register listener to listen changes of story points
     * and get details of that specific story point.
     * Once every sub story points are loaded,
     * exit the detail view of story point automatically.
     */
    fun setMiStoryPlayerListener(miStoryPlayerListener: MiStoryPlayerListener?) {
        this.mMiStoryPlayerListener = miStoryPlayerListener
    }

    /**
     * Move to next story point of particular story
     */
    fun moveToNextStoryPoint() {
        // Make this variable false again
        // to proceed further
        isCancelled = false

        if (currentIndex == mProgressbarCount) {
            if (mMiStoryPlayerListener != null) {
                // Once user reaches at the last story point of particular story,
                // exit from story detail view or move the next story.
                Log.e("TAG", "**** 2 - Last index :: true ****")
                mMiStoryPlayerListener?.onFinishedPlaying(isAtLastIndex = true)
                return
            }
        } else {
            // First finish currently running animation,
            // then start next animation.
            if (mProgressAnimators != null) {
                mProgressAnimators?.end()

                (mProgressAnimators as ValueAnimator).apply {
                    interpolator = LinearInterpolator()
                    addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {}

                        override fun onAnimationEnd(animation: Animator?) {
                            // Do nothing here
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                            isCancelled = true
                        }

                        override fun onAnimationRepeat(animation: Animator?) {}
                    })

                    if (!isRunning || !isStarted || isCancelled)
                        postDelayed({
                            startAnimating(currentIndex) // Move to next story
                        }, 200)
                }
            }
        }
    }

    /**
     * Move to previous story point of particular story
     */
    fun moveToPreviousStoryPoint(moveToIndex: Int) {
        currentIndex = moveToIndex

        if (currentIndex == 0) {
            // Move to previous story if exists,
            // If you are at the 1st index.
            Log.e("TAG", "**** 3 - Last index :: false ****")
            mMiStoryPlayerListener?.onFinishedPlaying(isAtLastIndex = false)
        } else {
            // Move to previous story point until
            // you reach at the 1st index
            if (mProgressAnimators != null) {
                resetProgressAnimation()
            }
        }
    }

    /**
     * Reset progress of current index
     * when moving to previous story
     */
    private fun resetProgressAnimation() {
        mProgressAnimators?.apply {
            interpolator = LinearInterpolator()
            duration = mSingleStoryDisplayTime
            cancel() // Cancel current animation
            mProgressBarRightEdge[currentIndex] = 0 // Reset progress of current animation to 0
            currentIndex -= 1 // Decrease current progress index by 1
            invalidate()
            isCancelled = false
        }
    }

    /**
     * Reset whole progress
     * and start over again.
     */
    fun startOverProgress() {
        for (i in 0 until mProgressbarCount) {
            mProgressAnimators?.apply {
                interpolator = LinearInterpolator()
                duration = mSingleStoryDisplayTime
                cancel() // Cancel current animation
                mProgressBarRightEdge[i] = 0 // Reset progress of current animation to 0.
                currentIndex = 0 // Reset current index to start over again.
                invalidate()
                isCancelled = false
            }
        }
    }

    /**
     * Prefill progress view if story
     * point already seen
     */
    fun prefillProgressView(upToIndex: Int) {
        if (upToIndex >= INITIAL_STORY_INDEX) {
            for (index in 0..upToIndex) {
                mProgressBarRightEdge[index] =
                    (index + 1) * mGapBetweenProgressBars + index * mSingleProgressBarWidth + mSingleProgressBarWidth
            }
        }
    }

    /**
     * Listener of StoryPlayer
     */
    interface MiStoryPlayerListener {
        fun onStartedPlaying(index: Int)
        fun onStoryFinished(index: Int)
        fun onFinishedPlaying(isAtLastIndex: Boolean)
        fun getCurrentTime(elapsedTime: Long, totalDuration: Long)
    }
}