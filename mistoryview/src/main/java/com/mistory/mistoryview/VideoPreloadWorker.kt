package com.mistory.mistoryview

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.*
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheWriter
import com.mistory.mistoryview.common.CacheUtils

class VideoPreloadWorker(private val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    private lateinit var mHttpDataSourceFactory: HttpDataSource.Factory
    private lateinit var mDefaultDataSourceFactory: DefaultDataSource
    private lateinit var mCacheDataSource: CacheDataSource

    companion object {
        const val VIDEO_URL = "video_url"

        fun buildWorkRequest(yourParameter: String?): OneTimeWorkRequest {
            val data = Data.Builder().putString(VIDEO_URL, yourParameter).build()
            return OneTimeWorkRequestBuilder<VideoPreloadWorker>().apply { setInputData(data) }
                .build()
        }
    }

    override fun doWork(): Result {
        try {
            val videoUrl: String? = inputData.getString(VIDEO_URL)

            mHttpDataSourceFactory = DefaultHttpDataSource.Factory()
                .setAllowCrossProtocolRedirects(true)

            mDefaultDataSourceFactory =
                DefaultDataSource.Factory(context, mHttpDataSourceFactory).createDataSource()

            mCacheDataSource = CacheDataSource.Factory()
                .setCache(CacheUtils.getSimpleCache())
                .setUpstreamDataSourceFactory(mHttpDataSourceFactory)
                .createDataSource()

            preCacheVideo(videoUrl)

            return Result.success()

        } catch (e: Exception) {
            return Result.failure()
        }
    }

    private fun preCacheVideo(videoUrl: String?) {
        val videoUri = Uri.parse(videoUrl)
        val dataSpec = DataSpec(videoUri)

        val progressListener = CacheWriter.ProgressListener { requestLength, bytesCached, _ ->
            val downloadPercentage: Double = (bytesCached * 100.0 / requestLength)
            Log.e(javaClass.simpleName, "Caching progress : $downloadPercentage")
            // Do Something
        }

        Thread().run {
            cacheVideo(dataSpec, progressListener)
            preCacheVideo(videoUrl)
        }
//        GlobalScope.launch(Dispatchers.IO) {
//            cacheVideo(dataSpec, progressListener)
//            preCacheVideo(videoUrl)
//        }
    }

    private fun cacheVideo(mDataSpec: DataSpec, mProgressListener: CacheWriter.ProgressListener) {
        runCatching {
            CacheWriter(
                mCacheDataSource,
                mDataSpec,
                null,
                mProgressListener,
            ).cache()
        }.onFailure {
            it.printStackTrace()
        }
    }
}