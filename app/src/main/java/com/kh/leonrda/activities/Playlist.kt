package com.kh.leonrda.activities

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.kh.leonrda.R
import com.kh.leonrda.adapters.TracksAdapter
import com.kh.leonaudioplayer.LeonTrack
import com.kh.leonrda.utils.FontUtil
import kotlinx.android.synthetic.main.actionbar.view.*
import kotlinx.android.synthetic.main.activity_playlist.*

class Playlist : AppCompatActivity() {

    var tracksLM: LinearLayoutManager? = null
    var tracksAdapter: TracksAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        setupActionbar()
        setupTracksRV()
        player.tracks = generateTracksArray()
    }

    fun setupActionbar() {
        val view = LayoutInflater.from(this).inflate(R.layout.actionbar, null)

        view.activityTitle.typeface = FontUtil.getTypeFace(this, "kingthings-calligraphica-2.ttf")
    }

    fun setupTracksRV() {
        tracksLM = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksAdapter = TracksAdapter(this, generateTracksArray())
        tracksRV.layoutManager = tracksLM
        tracksRV.adapter = tracksAdapter
    }

    fun generateTracksArray() : ArrayList<LeonTrack> {
        return arrayListOf(LeonTrack("L'enrolement", Uri.parse("///android_asset/episodes/RDA01.mp3")),
                LeonTrack("La foret des eventres", Uri.parse("///android_asset/episodes/RDA02.mp3")),
                LeonTrack("Le fleuve des glaires tiedes", Uri.parse("///android_asset/episodes/RDA03.mp3")),
                LeonTrack("La grotte de l'herpes ecorche", Uri.parse("///android_asset/episodes/RDA04.mp3")),
                LeonTrack("Bivouac...tion !", Uri.parse("///android_asset/episodes/RDA05.mp3")),
                LeonTrack("La colline des mille gangrenes", Uri.parse("///android_asset/episodes/RDA06.mp3")),
                LeonTrack("Le mont Mucus", Uri.parse("///android_asset/episodes/RDA07.mp3")),
                LeonTrack("Au fond du trou...", Uri.parse("///android_asset/episodes/RDA08.mp3")),
                LeonTrack("Enigme dans l'abime", Uri.parse("///android_asset/episodes/RDA09.mp3")),
                LeonTrack("La croisee des chemins", Uri.parse("///android_asset/episodes/RDA10.mp3")),
                LeonTrack("L'elfe et la prison brisee", Uri.parse("///android_asset/episodes/RDA11.mp3")),
                LeonTrack("L'infame alliance des fleaux", Uri.parse("///android_asset/episodes/RDA12.mp3")),
                LeonTrack("Dies irae, Dies illa", Uri.parse("///android_asset/episodes/RDA13.mp3")),
                LeonTrack("Un peuple...", Uri.parse("///android_asset/episodes/RDA14.mp3")),
                LeonTrack("Bien plus...", Uri.parse("///android_asset/episodes/RDA15.mp3")),
                LeonTrack("Au dela de la mort...", Uri.parse("///android_asset/episodes/RDA16.mp3")))
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean("ServiceState", player.serviceBound)
        super.onSaveInstanceState(savedInstanceState)
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        player.serviceBound = savedInstanceState.getBoolean("ServiceState")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (player.serviceBound) {
            unbindService(player.serviceConnection)
            player.mediaService?.stopSelf()
        }
    }
}


