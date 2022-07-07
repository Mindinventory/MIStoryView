package com.mistory.mistoryview.tools

interface AppMediaPlayerListener {
    fun isAudioPlaying(isPlay: Boolean)
    fun onMediaSeekListener(currentSeekPosition:Long, totalDuration:Long)
    fun onMediaSourceData(totalDuration:Long)
    fun movedToNext()
    fun movedToPrevious()
    fun playListOver()
    fun onMediaBuffering()
    fun onMediaItemTransition()
}
