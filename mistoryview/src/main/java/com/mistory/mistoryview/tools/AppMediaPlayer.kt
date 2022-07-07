package com.mistory.mistoryview.tools

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.PLAY_WHEN_READY_CHANGE_REASON_END_OF_MEDIA_ITEM

/**
 * Convenient media player class
 */
class AppMediaPlayer(
    private val player: ExoPlayer
) : Player.Listener {

    var currentPosition = 0L;

    private var listener: AppMediaPlayerListener? = null

    private val handler: Handler by lazy { return@lazy Handler(Looper.getMainLooper()) }

    private val runnable = Runnable { updateSeekBar() }

    init {
        player.addListener(this)
    }

    fun setAppMediaPlayerListener(listener: AppMediaPlayerListener?) {
        this.listener = listener
    }

    private fun updateSeekBar() {
        listener?.onMediaSeekListener(
            player.currentPosition,
            player.duration
        )
        currentPosition = player.currentPosition
        handler.postDelayed(runnable, 1000)
    }

    fun addMediaItem(mediaItem: MediaItem, playWhenReady: Boolean = false) {
        player.addMediaItem(mediaItem)
        postAddMediaItems(playWhenReady)
    }

    fun addMediaItems(mediaItems: List<MediaItem>, playWhenReady: Boolean = false) {
        player.addMediaItems(mediaItems)
        postAddMediaItems(playWhenReady)
    }

    private fun postAddMediaItems(playWhenReady: Boolean = false) {
        currentPosition = 0L
        player.prepare()
        player.playWhenReady = playWhenReady
        if (playWhenReady) {
            updateSeekBar()
        }
    }

    fun release() {
        player.release()
    }

    fun playPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            updateSeekBar()
            player.play()
        }
    }

    fun hasPrevious(): Boolean {
        return player.hasPreviousMediaItem()
    }

    fun hasNext(): Boolean {
        return player.hasNextMediaItem()
    }

    fun jumpToNext() {
        if (hasNext()) {
            player.seekToNextMediaItem()
            listener?.movedToNext()
        }
    }

    fun jumpToPrevious() {
        if (hasPrevious()) {
            player.seekToPreviousMediaItem()
            listener?.movedToPrevious()
        }
    }

    fun getCurrentMediaIndex(): Int {
        return player.currentMediaItemIndex
    }

    fun play() {
        player.play()
    }

    fun forward() {
        player.seekTo(player.currentPosition + 10000)
    }

    fun backward() {
        player.seekTo(player.currentPosition - 10000)
    }

    fun playAtSpecificIndex(index: Int) {
        player.seekToDefaultPosition(index)
    }

    fun retrieveTotalDuration(): Long {
        return player.duration
    }

    fun retrieveCurrentDuration() = player.currentPosition

    fun seekTo(position: Long) {
        player.seekTo(position)
        handler.removeCallbacks(runnable)
        updateSeekBar()
    }

    fun seekWithPlay(position: Long, forcePlay: Boolean = false) {
        handler.removeCallbacks(runnable)
        player.seekTo(position)
        if (forcePlay) {
            updateSeekBar()
        } else if (!player.isPlaying) {
            updateSeekBar()
        }
        player.play()

    }

    fun seekWithPause(position: Long) {
        player.seekTo(position)
        if (player.isPlaying) {
            player.pause()
        }
    }

    fun isPlaying(): Boolean = player.isPlaying

    fun pause() {
        if (player.isPlaying) {
            player.pause()
            handler.removeCallbacks(runnable)
        }
    }

    fun resume() {
        listener?.onMediaSeekListener(currentPosition, player.duration)
    }

    fun onDetachView() {
        player.stop()
        handler.removeCallbacks(runnable)
    }

    fun stop() {
        player.seekTo(0)
        player.pause()
        player.stop()
        handler.removeCallbacks(runnable)
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

        listener?.isAudioPlaying(playWhenReady)

        if (playWhenReady) {
        } else {
            handler.removeCallbacks(runnable)
        }

        when (playbackState) {
            ExoPlayer.STATE_IDLE -> {
                Log.e(javaClass.simpleName, "ExoPlayer.STATE_IDLE")
                handler.removeCallbacks(runnable)
            }
            ExoPlayer.STATE_ENDED -> {
                onPlayWhenReadyChanged(false, PLAY_WHEN_READY_CHANGE_REASON_END_OF_MEDIA_ITEM)
                listener?.onMediaSeekListener(0, player.duration)
                listener?.isAudioPlaying(
                    false
                )
                handler.removeCallbacks(runnable)
                Log.e(javaClass.simpleName, "ExoPlayer.STATE_ENDED")
            }
            ExoPlayer.STATE_READY -> {
                Log.e(javaClass.simpleName, "ExoPlayer.STATE_READY")
                onPlayWhenReadyChanged(true, PLAY_WHEN_READY_CHANGE_REASON_END_OF_MEDIA_ITEM)
                listener?.onMediaSourceData(player.duration)
            }
            ExoPlayer.STATE_BUFFERING -> {
                listener?.onMediaBuffering()
            }
        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        listener?.onMediaItemTransition()
    }
}
