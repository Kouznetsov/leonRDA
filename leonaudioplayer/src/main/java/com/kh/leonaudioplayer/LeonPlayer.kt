package com.kh.leonaudioplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.media.session.MediaController
import android.os.IBinder
import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.leon_player_view.view.*
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class LeonPlayer(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs),
        SeekBar.OnSeekBarChangeListener {

    var tracks: List<LeonTrack> = ArrayList()
    var mediaService: LeonMediaService? = null
    var serviceBound = false

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LeonMediaService.LocalBinder
            mediaService = binder.service
            serviceBound = true
            Toast.makeText(context, "Service Bound", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceBound = false
        }
    }

    private fun play(track: LeonTrack) {
        //Check if service is active
        if (!serviceBound) {
            val playerIntent = Intent(context, LeonMediaService::class.java)

            playerIntent.putExtra("tracks", tracks as Serializable)
            context.startService(playerIntent)
            context.bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE)
            currentTrackTitle.text = track.title
            if (mediaService != null && mediaService?.mediaPlayer != null) {
                progressText.text = String.format(Locale.getDefault(), "00:00/%s",
                        milisToMinuteText("Nique ta mere"))
                progresSeekbar.progress = 0
            }
        } else {
            //Service is active
            //Send media with BroadcastReceiver
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.leon_player_view, this)
        progresSeekbar.setOnSeekBarChangeListener(this)
    }

    private fun milisToMinuteText(milis: Int): String {
        val totalSeconds = milis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        return "$minutes:$seconds"
    }

    fun playPlaylist(tracks: List<LeonTrack>) {
        this.tracks = tracks


    }

    fun playTrack(track: LeonTrack) {

    }

    override fun onProgressChanged(bar: SeekBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(bar: SeekBar?) {
    }

    override fun onStopTrackingTouch(bar: SeekBar?) {
    }
}