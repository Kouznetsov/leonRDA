package com.kh.leonaudioplayer

import android.net.Uri
import java.io.Serializable

data class LeonTrack(val title: String, val uri: Uri) : Serializable
