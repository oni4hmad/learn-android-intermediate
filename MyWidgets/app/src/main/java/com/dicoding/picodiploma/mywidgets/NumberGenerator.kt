package com.dicoding.picodiploma.mywidgets

import android.appwidget.AppWidgetManager
import java.util.*

internal object NumberGenerator {
    fun generate(max: Int): Int {
        val random = Random()
        return random.nextInt(max)
    }
}