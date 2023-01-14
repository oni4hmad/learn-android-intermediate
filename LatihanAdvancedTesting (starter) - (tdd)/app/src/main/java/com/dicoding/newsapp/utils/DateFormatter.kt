package com.dicoding.newsapp.utils

import okhttp3.internal.format
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object DateFormatter {

    /*
    * -- problem: test hanya support WIB, kalo WITA maka test gagal, karena local time berbeda
    * -- solusi: menerapkan depedency injection
    * --- Keluarkan TimeZone.getDefault().id menjadi targetTimeZone
    * --- (mengeluarkan hal yang berhubungan dengan Locale agar kode mudah ditest/tidak Flacky)
    * */

    fun formatDate(currentDateString: String, targetTimeZone: String): String {
        val instant = Instant.parse(currentDateString)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm" )
//            .withZone(ZoneId.of(TimeZone.getDefault().id))
            .withZone(ZoneId.of(targetTimeZone))
        return formatter.format(instant)
    }
}