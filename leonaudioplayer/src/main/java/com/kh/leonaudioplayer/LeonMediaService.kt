package com.kh.leonaudioplayer

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import java.io.IOException


class LeonMediaService : Service(), MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener {

    var mediaPlayer: MediaPlayer
    var tracks: List<LeonTrack>
    var resumePosition: Int = 0

    init {
        mediaPlayer = MediaPlayer()
        tracks = ArrayList()
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setOnErrorListener(this)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnBufferingUpdateListener(this)
        mediaPlayer.setOnSeekCompleteListener(this)
        mediaPlayer.setOnInfoListener(this)
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

    fun play() {
    }

    private val iBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return iBinder
    }
    override fun onCompletion(p0: MediaPlayer?) {

    }

    override fun onPrepared(p0: MediaPlayer?) {
    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
    }

    override fun onSeekComplete(p0: MediaPlayer?) {
    }

    override fun onInfo(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
    }

    override fun onBufferingUpdate(p0: MediaPlayer?, p1: Int) {
    }

    override fun onAudioFocusChange(p0: Int) {
    }

    inner class LocalBinder : Binder() {
        val service: LeonMediaService
            get() = this@LeonMediaService
    }

}