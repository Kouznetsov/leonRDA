package com.kh.leonaudioplayer

import android.content.Context
import android.media.MediaPlayer
import android.media.session.MediaController
import android.support.v4.media.MediaBrowserCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.SeekBar
import kotlinx.android.synthetic.main.leon_player_view.view.*
import java.util.*

class LeonPlayer(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs),
        SeekBar.OnSeekBarChangeListener {

    lateinit var tracks: List<LeonTrack>
    var mediaPlayer: MediaPlayer
    lateinit var mediaBrowser: MediaBrowserCompat


    init {
        LayoutInflater.from(context).inflate(R.layout.leon_player_view, this)
        mediaPlayer = MediaPlayer()
        progresSeekbar.setOnSeekBarChangeListener(this)
        mediaBrowser = MediaBrowserCompat()
    }

    private fun milisToMinuteText(milis: Int) : String {
        val totalSeconds = milis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        return "$minutes:$seconds"
    }

    fun playPlaylist(tracks: List<LeonTrack>) {
        this.tracks = tracks

    }

    fun playTrack(track: LeonTrack) {
        mediaPlayer.setDataSource(context, track.uri)
        currentTrackTitle.text = track.title
        progressText.text = String.format(Locale.getDefault(), "00:00/%s",
                milisToMinuteText(mediaPlayer.duration))
        progresSeekbar.progress = 0
        mediaPlayer.start()
    }

    override fun onProgressChanged(bar: SeekBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(bar: SeekBar?) {
    }

    override fun onStopTrackingTouch(bar: SeekBar?) {
    }
}