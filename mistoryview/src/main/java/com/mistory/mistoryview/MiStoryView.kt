package com.mistory.mistoryview

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.example.mistoryview.R
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheWriter
import com.mistory.mistoryview.common.CacheUtils
import com.mistory.mistoryview.common.extension.ImageLoadingListener
import com.mistory.mistoryview.common.extension.dpToPx
import com.mistory.mistoryview.common.extension.loadThumbnailImage
import com.mistory.mistoryview.data.entity.MiStoryModel
import com.mistory.mistoryview.data.entity.MiUserStoryModel
import com.mistory.mistoryview.ui.MiStoryHorizontalProgressView
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.HORIZONTAL_PROGRESS_ATTRIBUTES
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.INDEX_OF_SELECTED_STORY
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.MI_FULLSCREEN_GAP_BETWEEN_PROGRESSBAR
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.MI_FULLSCREEN_PROGRESSBAR_HEIGHT
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.MI_FULLSCREEN_PROGRESSBAR_PRIMARY_COLOR
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.MI_FULLSCREEN_PROGRESSBAR_SECONDARY_COLOR
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.MI_FULLSCREEN_SINGLE_STORY_DISPLAY_TIME
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.MI_LIST_OF_STORY
import com.mistory.mistoryview.ui.activity.MiStoryDisplayActivity.Companion.PAGE_TRANSFORMER
import kotlinx.parcelize.Parcelize

private const val PRECACHE_VIDEO_WORKER_NAME = "precache_video_worker"

class MiStoryView : View {
    companion object {
        const val MI_STORY_IMAGE_RADIUS_IN_DP = 36
        const val MI_STORY_INDICATOR_WIDTH_IN_DP = 4
        const val SPACE_BETWEEN_IMAGE_AND_INDICATOR = 4
        const val MI_START_ANGLE = 270

        const val MI_PENDING_INDICATOR_COLOR = "#009988"
        const val MI_VISITED_INDICATOR_COLOR = "#33009988"
    }

    private var mAppCompatActivity: AppCompatActivity? = null
    private var resource: Resources? = null
    private var miPaintIndicator: Paint? = null
    private var miPageTransformer = PageTransformer.BACKGROUND_TO_FOREGROUND_TRANSFORMER
    private var miStoryImageRadiusInPx = 0
    private var miStoryIndicatorWidthInPx = 0
    private var miSpaceBetweenImageAndIndicator = 0
    private var miPendingIndicatorColor = 0
    private var miVisitedIndicatorColor = 0

    private var miStoryViewHeight = 0
    private var miStoryViewWidth = 0
    private var miIndicatorOffset = 0
    private var miIndicatorImageOffset = 0
    private var miIndicatorImageRect: Rect? = null

    private var miStoryImageUrls: ArrayList<MiStoryModel>? = null
    private var miStoryUrls: ArrayList<MiUserStoryModel>? = null
    private var miIndicatorCount = 0
    private var miIndicatorBendAngle = 0
    private var miAngleOfGap = 15
    private var miIndicatorImageBitmap: Bitmap? = null
    private var indexOfSelectedStory = -1
    private var launcher: ActivityResultLauncher<Intent>? = null

    //MiStoryHorizontalProgressView attributes
    private var miFullScreenProgressBarHeight = 0
    private var miFullScreenGapBetweenProgressBar = 0
    private var miFullScreenProgressBarPrimaryColor = 0
    private var miFullScreenProgressBarSecondaryColor = 0
    private var miFullScreenSingleStoryDisplayTime: Long = 0

    constructor(context: Context) : super(context) {
//        CacheUtils.initializeCache(context)
        initView(context)
        setDefaults()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MiStoryView, 0, 0)
        try {
            resource?.let {
                miPageTransformer = PageTransformer.values()[typedArray.getInt(
                    R.styleable.MiStoryView_miPageTransformer, 0
                )]

                miStoryImageRadiusInPx = typedArray.getDimension(
                    R.styleable.MiStoryView_miStoryImageRadius,
                    MI_STORY_IMAGE_RADIUS_IN_DP.toFloat()
                ).toInt().dpToPx(it)

                miStoryIndicatorWidthInPx = typedArray.getDimension(
                    R.styleable.MiStoryView_miStoryItemIndicatorWidth,
                    MI_STORY_INDICATOR_WIDTH_IN_DP.toFloat()
                ).toInt().dpToPx(it)

                miSpaceBetweenImageAndIndicator = typedArray.getDimension(
                    R.styleable.MiStoryView_miSpaceBetweenImageAndIndicator,
                    SPACE_BETWEEN_IMAGE_AND_INDICATOR.toFloat()
                ).toInt().dpToPx(it)

                miPendingIndicatorColor = typedArray.getColor(
                    R.styleable.MiStoryView_miPendingIndicatorColor,
                    Color.parseColor(MI_PENDING_INDICATOR_COLOR)
                )

                miVisitedIndicatorColor = typedArray.getColor(
                    R.styleable.MiStoryView_miVisitedIndicatorColor,
                    Color.parseColor(MI_VISITED_INDICATOR_COLOR)
                )

                // Configure MiStoryHorizontalProgressView attributes here
                miFullScreenProgressBarHeight = typedArray.getDimension(
                    R.styleable.MiStoryView_miFullScreenProgressBarHeight,
                    MiStoryHorizontalProgressView.MI_PROGRESS_BAR_HEIGHT.toFloat()
                ).toInt().dpToPx(it)

                miFullScreenGapBetweenProgressBar = typedArray.getDimension(
                    R.styleable.MiStoryView_miFullScreenGapBetweenProgressBar,
                    MiStoryHorizontalProgressView.MI_GAP_BETWEEN_PROGRESS_BARS.toFloat()
                ).toInt().dpToPx(it)

                miFullScreenProgressBarPrimaryColor = typedArray.getColor(
                    R.styleable.MiStoryView_miFullScreenProgressBarPrimaryColor,
                    Color.parseColor(MiStoryHorizontalProgressView.MI_PROGRESS_PRIMARY_COLOR)
                )

                miFullScreenProgressBarSecondaryColor = typedArray.getColor(
                    R.styleable.MiStoryView_miFullScreenProgressBarSecondaryColor,
                    Color.parseColor(MiStoryHorizontalProgressView.MI_PROGRESS_SECONDARY_COLOR)
                )

                miFullScreenSingleStoryDisplayTime = typedArray.getInt(
                    R.styleable.MiStoryView_miFullScreenSingleStoryDisplayTime,
                    MiStoryHorizontalProgressView.MI_SINGLE_STORY_DISPLAY_TIME
                ).toLong()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
        prepareValue()
    }

    fun setActivity(activity: AppCompatActivity) {
        mAppCompatActivity = activity
    }

    fun setLauncher(launcher: ActivityResultLauncher<Intent>) {
        this.launcher = launcher
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        miPaintIndicator?.apply {
            color = miPendingIndicatorColor
            strokeWidth = miStoryIndicatorWidthInPx.toFloat()
            var startAngle = MI_START_ANGLE + miAngleOfGap / 2
            for (i in 0 until miIndicatorCount) {
                color = getIndicatorColor(i)
                canvas.drawArc(
                    miIndicatorOffset.toFloat(), miIndicatorOffset.toFloat(),
                    (miStoryViewWidth - miIndicatorOffset).toFloat(),
                    (miStoryViewHeight - miIndicatorOffset).toFloat(),
                    startAngle.toFloat(),
                    (miIndicatorBendAngle - miAngleOfGap / 2).toFloat(),
                    false,
                    this
                )
                startAngle += miIndicatorBendAngle + miAngleOfGap / 2
            }
            miIndicatorImageBitmap?.let { bitmap ->
                miIndicatorImageRect?.let { rect ->
                    canvas.drawBitmap(bitmap, null, rect, null)
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = paddingStart + paddingEnd + miStoryViewWidth
        val height = paddingTop + paddingBottom + miStoryViewHeight
        val widthMeasure = resolveSizeAndState(width, widthMeasureSpec, 0)
        val heightMeasure = resolveSizeAndState(height, heightMeasureSpec, 0)
        setMeasuredDimension(widthMeasure, heightMeasure)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            navigationToStoryDisplay()
        }
        return true
    }

    private fun navigationToStoryDisplay() {
        if (mAppCompatActivity == null) {
            throw RuntimeException("Context must not be null.")
        } else {
            val bundle = Bundle()
            bundle.putParcelable(PAGE_TRANSFORMER, miPageTransformer)

            val intent = Intent(mAppCompatActivity, MiStoryDisplayActivity::class.java)
            intent.putParcelableArrayListExtra(MI_LIST_OF_STORY, miStoryUrls)
            intent.putExtra(INDEX_OF_SELECTED_STORY, indexOfSelectedStory)
            intent.putExtra(
                HORIZONTAL_PROGRESS_ATTRIBUTES,
                retrieveHorizontalProgressViewAttributes()
            )
            intent.putExtras(bundle)
            launcher?.launch(intent)
        }
    }

    private fun retrieveHorizontalProgressViewAttributes(): HashMap<String, Any> {
        val horizontalProgressValuesHashMap = hashMapOf<String, Any>()

        horizontalProgressValuesHashMap[MI_FULLSCREEN_PROGRESSBAR_HEIGHT] =
            miFullScreenProgressBarHeight
        horizontalProgressValuesHashMap[MI_FULLSCREEN_GAP_BETWEEN_PROGRESSBAR] =
            miFullScreenGapBetweenProgressBar
        horizontalProgressValuesHashMap[MI_FULLSCREEN_PROGRESSBAR_PRIMARY_COLOR] =
            miFullScreenProgressBarPrimaryColor
        horizontalProgressValuesHashMap[MI_FULLSCREEN_PROGRESSBAR_SECONDARY_COLOR] =
            miFullScreenProgressBarSecondaryColor
        horizontalProgressValuesHashMap[MI_FULLSCREEN_SINGLE_STORY_DISPLAY_TIME] =
            miFullScreenSingleStoryDisplayTime

        return horizontalProgressValuesHashMap
    }

    private fun initView(context: Context) {
        resource = context.resources
        miPaintIndicator = Paint()
        miPaintIndicator?.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
    }

    private fun setDefaults() {
        resource?.let {
            miStoryImageRadiusInPx = MI_STORY_IMAGE_RADIUS_IN_DP.dpToPx(it)
            miStoryIndicatorWidthInPx = MI_STORY_INDICATOR_WIDTH_IN_DP.dpToPx(it)
            miSpaceBetweenImageAndIndicator = SPACE_BETWEEN_IMAGE_AND_INDICATOR.dpToPx(it)
            miPendingIndicatorColor = Color.parseColor(MI_PENDING_INDICATOR_COLOR)
            miVisitedIndicatorColor = Color.parseColor(MI_VISITED_INDICATOR_COLOR)
        }
        prepareValue()
    }

    private fun prepareValue() {
        miStoryViewHeight =
            2 * (miStoryIndicatorWidthInPx + miSpaceBetweenImageAndIndicator + miStoryImageRadiusInPx)
        miStoryViewWidth = miStoryViewHeight
        miIndicatorOffset = miStoryIndicatorWidthInPx / 2
        miIndicatorImageOffset = miStoryIndicatorWidthInPx + miSpaceBetweenImageAndIndicator
        miIndicatorImageRect = Rect(
            miIndicatorImageOffset,
            miIndicatorImageOffset,
            miStoryViewWidth - miIndicatorImageOffset,
            miStoryViewHeight - miIndicatorImageOffset
        )
    }

    fun setImageUrls(listOfStory: ArrayList<MiUserStoryModel>, indexOfSelectedStory: Int = 0) {
        miStoryUrls = listOfStory
        this.indexOfSelectedStory = indexOfSelectedStory
        miStoryImageUrls = listOfStory[indexOfSelectedStory].userStoryList
        miIndicatorCount = listOfStory[indexOfSelectedStory].userStoryList.size
        calculateBendAngle(miIndicatorCount)
        invalidate()
        loadLastImageBitmap()

        // precache videos if there are any
//        preloadVideos()
    }

    private fun preloadVideos() {
        miStoryImageUrls?.map { data ->
            Thread().run {
                val dataUri = Uri.parse(data.mediaUrl)
                val dataSpec = DataSpec(dataUri)

                val mHttpDataSourceFactory = DefaultHttpDataSource.Factory()
                    .setAllowCrossProtocolRedirects(true)

                val mCacheDataSource = CacheDataSource.Factory()
                    .setCache(CacheUtils.getSimpleCache())
                    .setUpstreamDataSourceFactory(mHttpDataSourceFactory)
                    .createDataSource()

                val listener =
                    CacheWriter.ProgressListener { requestLength: Long, bytesCached: Long, _: Long ->
                        val downloadPercentage = (bytesCached * 100.0 / requestLength)
                        Log.e(javaClass.simpleName, "downloadPercentage: $downloadPercentage")
                    }

                try {
                    CacheWriter(
                        mCacheDataSource,
                        dataSpec,
                        null,
                        listener,
                    ).cache()

                    /*CacheUtil.cache(
                        dataSpec,
                        StoryApp.simpleCache,
                        CacheUtil.DEFAULT_CACHE_KEY_FACTORY,
                        dataSource,
                        listener,
                        null
                    )*/
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


        /*miStoryImageUrls?.map {
            if (it.mediaType == MiMediaType.VIDEO) {
                val workManager = WorkManager.getInstance(context.applicationContext)
                val videoPreloadWorker = VideoPreloadWorker.buildWorkRequest(it.mediaUrl)
                workManager.enqueueUniqueWork(
                    PRECACHE_VIDEO_WORKER_NAME, ExistingWorkPolicy.KEEP, videoPreloadWorker
                )
            }
        }*/
    }

    private fun loadLastImageBitmap() {
        loadThumbnailImage(
            context,
            miStoryImageUrls?.first()?.mediaUrl,
            object : ImageLoadingListener {
                override fun onResourceReady(bitmap: Bitmap) {
                    miIndicatorImageBitmap = bitmap
                    invalidate()
                }
            })
    }

    private fun calculateBendAngle(indicatorCount: Int) {
        miAngleOfGap = if (indicatorCount == 1) {
            0
        } else {
            15
        }
        miIndicatorBendAngle = 360 / indicatorCount - miAngleOfGap / 2
    }

    private fun getIndicatorColor(index: Int): Int {
        return if (miStoryImageUrls?.get(index)?.isStorySeen == true)
            miVisitedIndicatorColor
        else
            miPendingIndicatorColor
    }

    @Parcelize
    enum class PageTransformer : Parcelable {
        BACKGROUND_TO_FOREGROUND_TRANSFORMER,
        CUBE_OUT_TRANSFORMER,
        FOREGROUND_TO_BACKGROUND_TRANSFORMER,
        ZOOM_OUT_PAGE_TRANSFORMER,
        CUBE_IN_TRANSFORMER,
        ROTATE_DOWN_PAGE_TRANSFORMER,
        ROTATE_UP_PAGE_TRANSFORMER,
        ZOOM_IN_TRANSFORMER
    }
}