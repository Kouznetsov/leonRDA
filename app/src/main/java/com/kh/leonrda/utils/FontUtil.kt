package com.kh.leonrda.utils

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView

class FontUtil {

    companion object {

        private var fontsCache: MutableMap<String, Typeface> = HashMap()

        fun getTypeFace(context: Context, name: String): Typeface? {
            if (fontsCache.containsKey(name) && fontsCache[name] != null)
                return fontsCache[name]
            fontsCache[name] = Typeface.createFromAsset(context.assets, "fonts/" + name)
            return fontsCache[name]
        }

    }


    fun TextView.applyTypeface(name: String) {
        this.typeface = getTypeFace(this.context, name)
    }
}