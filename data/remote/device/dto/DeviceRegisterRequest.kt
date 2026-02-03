package com.itirafapp.android.data.remote.device.dto

data class DeviceRegisterRequest(
    val token: String,
    val platform: String,
    val appVersion: String,
    val deviceModel: String,
    val osVersion: String,
    val pushEnabled: Boolean
)