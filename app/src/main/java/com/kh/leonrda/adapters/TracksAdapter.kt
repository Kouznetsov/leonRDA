package com.kh.leonrda.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kh.leonrda.R
import com.kh.leonaudioplayer.LeonTrack
import com.kh.leonrda.utils.FontUtil
import kotlinx.android.synthetic.main.track_row.view.*

class TracksAdapter(private val context: Context, private val tracks: List<LeonTrack>) : RecyclerView.Adapter<TracksAdapter.TracksVH>() {

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TracksVH?, position: Int) {
        holder?.bindTitle(tracks[position].title)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TracksVH {
        return TracksVH(LayoutInflater.from(context).inflate(R.layout.track_row, parent, false))
    }

    class TracksVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindTitle(newTitle: String) {
            itemView.trackTitle.typeface = FontUtil.getTypeFace(itemView.context, "kingthings-calligraphica-2.ttf")
            itemView.trackTitle.text = newTitle
        }

    }
}