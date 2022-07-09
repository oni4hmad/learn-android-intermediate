package com.dicoding.picodiploma.mystackwidget

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageData(
    val drawableId: Int
) : Parcelable
