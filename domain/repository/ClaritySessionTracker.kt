package com.itirafapp.android.domain.repository

interface SessionTracker {
    fun setup()
    fun setUserId(userId: String)
    fun clearUser()
    fun setCurrentScreen(screenName: String)
}