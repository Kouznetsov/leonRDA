package com.kh.leonaudioplayer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.io.IOException


class LeonMediaService : Service(), MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener {

    val TAG = "LeonMediaService"

    var mediaPlayer: MediaPlayer
    var tracks: List<LeonTrack>
    var resumePosition: Int = 0
    var audioManager: AudioManager

    init {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mediaPlayer = MediaPlayer()
        tracks = ArrayList()
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setOnErrorListener(this)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnBufferingUpdateListener(this)
        mediaPlayer.setOnSeekCompleteListener(this)
        //Reset so that the MediaPlayer is not pointing to another data source
        mediaPlayer.reset()

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            // Set the data source to the mediaFile location
            mediaPlayer.setDataSource(tracks[0].uri.toString())
        } catch (e: IOException) {
            e.printStackTrace()
            stopSelf()
        }

        mediaPlayer.prepareAsync()
    }

    fun playMedia() {
        if (mediaPlayer.isPlaying)
            mediaPlayer.start()
    }

    fun stopMedia() {
        if (mediaPlayer.isPlaying)
            mediaPlayer.stop()
    }

    fun pauseMedia() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            resumePosition = mediaPlayer.currentPosition
        }
    }

    fun resumeMedia() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(resumePosition)
            mediaPlayer.start()
        }
    }

    private val iBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return iBinder
    }

    override fun onCompletion(p0: MediaPlayer?) {
        stopMedia()
        stopSelf()
    }

    override fun onPrepared(p0: MediaPlayer?) {
        playMedia()
    }

    override fun onError(mediaPlayer: MediaPlayer?, what: Int, extra: Int): Boolean {
        when (what) {
            MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK ->
                Log.e(TAG, "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra)
            MediaPlayer.MEDIA_ERROR_SERVER_DIED ->
                Log.e(TAG, "MEDIA ERROR SERVER DIED " + extra)
            MediaPlayer.MEDIA_ERROR_UNKNOWN ->
                Log.e(TAG, "MEDIA ERROR UNKNOWN " + extra)
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMedia()
        mediaPlayer.release()
        removeAudioFocus()
    }

    override fun onSeekComplete(p0: MediaPlayer?) {
    }

    override fun onBufferingUpdate(p0: MediaPlayer?, p1: Int) {
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        try {
            tracks = intent.extras!!.getSerializable("tracks") as ArrayList<LeonTrack>
        } catch (e: NullPointerException) {
            stopSelf()
        }
        if (!requestAudioFocus())
            stopSelf()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onAudioFocusChange(focusState: Int) {
        when (focusState) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                if (!mediaPlayer.isPlaying) mediaPlayer.start()
                mediaPlayer.setVolume(1.0f, 1.0f)
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                if (mediaPlayer.isPlaying) mediaPlayer.stop()
                mediaPlayer.release()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ->
                if (mediaPlayer.isPlaying) mediaPlayer.pause()
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->
                if (mediaPlayer.isPlaying) mediaPlayer.setVolume(0.1f, 0.1f)
        }
    }

    private fun removeAudioFocus(): Boolean {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this)
    }

    fun requestAudioFocus(): Boolean {
        val result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }


    inner class LocalBinder : Binder() {
        val service: LeonMediaService
            get() = this@LeonMediaService
    }

}