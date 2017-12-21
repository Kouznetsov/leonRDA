package com.kh.leonaudioplayer

import android.app.PendingIntent
import android.app.Service
import android.content.*
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.os.PowerManager
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.session.MediaButtonReceiver
import android.support.v4.media.session.MediaSessionCompat
import android.R.attr.start
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.media.session.PlaybackStateCompat

class LeonMediaService : MediaBrowserServiceCompat(),
        AudioManager.OnAudioFocusChangeListener {

    val TAG = "LeonMediaService"
    private val PENDINGINTENT_REQUESTCODE = 423

    lateinit private var _mediaSession: MediaSessionCompat
    lateinit private var _mediaPlayer: MediaPlayer
    private val _iBinder: IBinder = LocalBinder()
    private var _noisyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (_mediaPlayer.isPlaying)
                _mediaPlayer.pause()
        }
    }
    private var _callback = object : MediaSessionCompat.Callback() {

        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
        }

        override fun onPlay() {
            super.onPlay()
            if (!successfullyRetrievedAudioFocus())
                return
            _mediaSession.isActive = true
            setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING)
            showPlayingNotification()
            _mediaPlayer.start()
        }

        override fun onStop() {
            super.onStop()
        }

        override fun onSkipToNext() {
            super.onSkipToNext()
        }

        override fun onPause() {
            super.onPause()
            if (_mediaPlayer.isPlaying) {
                _mediaPlayer.pause()
                setMediaPlaybackState(PlaybackStateCompat.STATE_PAUSED)
                showPausedNotification()
            }
        }

        private fun setMediaPlaybackState(state: Int) {
            val playbackstateBuilder = PlaybackStateCompat.Builder()
            if (state == PlaybackStateCompat.STATE_PLAYING)
                playbackstateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_PAUSE)
            else
                playbackstateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_PLAY)
            playbackstateBuilder.setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0f)
            _mediaSession.setPlaybackState(playbackstateBuilder.build())
        }
    }

    private fun showPausedNotification() {
        val builder = MediaStyleHelper.from(this, _mediaSession) ?: return

        builder.addAction(NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)))
        builder.setStyle(android.support.v4.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0).setMediaSession(_mediaSession.getSessionToken()))
        builder.setSmallIcon(R.mipmap.ic_launcher)
        NotificationManagerCompat.from(this).notify(1, builder.build())
    }

    private fun showPlayingNotification() {
        val builder = MediaStyleHelper.from(this, _mediaSession) ?: return

        builder.addAction(NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)))
        builder.setStyle(android.support.v4.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0).setMediaSession(_mediaSession.getSessionToken()))
        builder.setSmallIcon(R.mipmap.ic_launcher)
        NotificationManagerCompat.from(this).notify(1, builder.build())
    }

    override fun onCreate() {
        super.onCreate()
        initMediaPlayer()
        initMediaSession()
        initNoisyReceiver()
    }

    fun initMediaPlayer() {
        _mediaPlayer = MediaPlayer()
        _mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK)
        _mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        _mediaPlayer.setVolume(1.0f, 1.0f)
    }

    fun initMediaSession() {
        val mediaButtonReceiver = ComponentName(this, MediaButtonReceiver::class.java)
        val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        val pendingIntent: PendingIntent

        _mediaSession = MediaSessionCompat(this, TAG, mediaButtonReceiver, null)
        _mediaSession.setCallback(_callback)
        _mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        mediaButtonIntent.setClass(this, MediaButtonReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this,
                PENDINGINTENT_REQUESTCODE,
                mediaButtonIntent, 0)
        _mediaSession.setMediaButtonReceiver(pendingIntent)
        sessionToken = _mediaSession.sessionToken
    }

    fun initNoisyReceiver() {
        registerReceiver(_noisyReceiver, IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY))
    }

    override fun onDestroy() {
        super.onDestroy()
        _mediaSession.release()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        MediaButtonReceiver.handleIntent(_mediaSession, intent)
        return _iBinder
    }

    fun successfullyRetrievedAudioFocus(): Boolean {
        val audioMgr = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val result = audioMgr.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)

        return result == AudioManager.AUDIOFOCUS_GAIN
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                if (_mediaPlayer.isPlaying)
                    _mediaPlayer.stop()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                _mediaPlayer.pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->
                _mediaPlayer.setVolume(0.3f, 0.3f)
            AudioManager.AUDIOFOCUS_GAIN -> {
                if (!_mediaPlayer.isPlaying)
                    _mediaPlayer.start()
                _mediaPlayer.setVolume(1.0f, 1.0f)
            }
        }
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        result.sendResult(null)
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        return null
    }

    inner class LocalBinder : Binder() {
        val service: LeonMediaService
            get() = this@LeonMediaService
    }
}