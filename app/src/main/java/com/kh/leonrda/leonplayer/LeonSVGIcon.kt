package com.kh.leonrda.leonplayer

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.kh.leonrda.utils.FontUtil

class LeonSVGIcon(context: Context?, attrs: AttributeSet) : TextView(context, attrs) {

    init {
        context?.let { typeface = FontUtil.getTypeFace(it, "icomoon.ttf") }
    }
}