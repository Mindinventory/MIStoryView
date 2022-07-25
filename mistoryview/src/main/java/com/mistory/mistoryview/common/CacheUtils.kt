package com.mistory.mistoryview.common

import android.content.Context
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache

object CacheUtils {

    // Pre-caching objects
    private const val cacheSize: Long = 90 * 1024 * 1024
    private var lruCache = LeastRecentlyUsedCacheEvictor(cacheSize)
    private lateinit var exoplayerDatabaseProvider: StandaloneDatabaseProvider
    lateinit var cache: SimpleCache

    fun initializeCache(context: Context) {
        exoplayerDatabaseProvider = StandaloneDatabaseProvider(context)
        cache = SimpleCache(context.cacheDir, lruCache, exoplayerDatabaseProvider)
    }

    fun getSimpleCache(): SimpleCache = cache
}
