package com.kh.leonaudioplayer

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import java.util.*

class LeonSVGIcon(context: Context?, attrs: AttributeSet) : TextView(context, attrs) {

    companion object {

        private var fontsCache: MutableMap<String, Typeface> = HashMap()

        fun getTypeFace(context: Context, name: String): Typeface? {
            if (fontsCache.containsKey(name) && fontsCache[name] != null)
                return fontsCache[name]
            fontsCache[name] = Typeface.createFromAsset(context.assets, name)
            return fontsCache[name]
        }

    }

    init {
        context?.let { typeface = getTypeFace(it, "icomoon.ttf") }
    }
}