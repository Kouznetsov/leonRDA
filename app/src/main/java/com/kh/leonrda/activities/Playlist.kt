package com.kh.leonrda.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.TextView
import com.kh.leonrda.R
import com.kh.leonrda.adapters.TracksAdapter
import com.kh.leonrda.models.Track
import com.kh.leonrda.utils.FontUtil
import kotlinx.android.synthetic.main.actionbar.view.*
import kotlinx.android.synthetic.main.activity_playlist.*
import java.net.URI

class Playlist : AppCompatActivity() {

    var tracksLM: LinearLayoutManager? = null
    var tracksAdapter: TracksAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        setupActionbar()
        setupTracksRV()
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

    fun generateTracksArray() : Array<Track> {
        return arrayOf(Track("L'enrolement", URI.create("///android_asset/episodes/RDA01.mp3")),
                Track("La foret des eventres", URI.create("///android_asset/episodes/RDA02.mp3")),
                Track("Le fleuve des glaires tiedes", URI.create("///android_asset/episodes/RDA03.mp3")),
                Track("La grotte de l'herpes ecorche", URI.create("///android_asset/episodes/RDA04.mp3")),
                Track("Bivouac...tion !", URI.create("///android_asset/episodes/RDA05.mp3")),
                Track("La colline des mille gangrenes", URI.create("///android_asset/episodes/RDA06.mp3")),
                Track("Le mont Mucus", URI.create("///android_asset/episodes/RDA07.mp3")),
                Track("Au fond du trou...", URI.create("///android_asset/episodes/RDA08.mp3")),
                Track("Enigme dans l'abime", URI.create("///android_asset/episodes/RDA09.mp3")),
                Track("La croisee des chemins", URI.create("///android_asset/episodes/RDA10.mp3")),
                Track("L'elfe et la prison brisee", URI.create("///android_asset/episodes/RDA11.mp3")),
                Track("L'infame alliance des fleaux", URI.create("///android_asset/episodes/RDA12.mp3")),
                Track("Dies irae, Dies illa", URI.create("///android_asset/episodes/RDA13.mp3")),
                Track("Un peuple...", URI.create("///android_asset/episodes/RDA14.mp3")),
                Track("Bien plus...", URI.create("///android_asset/episodes/RDA15.mp3")),
                Track("Au dela de la mort...", URI.create("///android_asset/episodes/RDA16.mp3")))
    }
}


