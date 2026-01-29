package com.itirafapp.android.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChannelData(
    val id: Int,
    val title: String,
    val description: String,
    val imageURL: String?
) : Parcelable
