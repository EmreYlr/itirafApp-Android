package com.itirafapp.android.util.manager

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itirafapp.android.domain.model.ChannelData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FollowPreferencesManager @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson
) {
    private val prefs = context.getSharedPreferences("follow_prefs", Context.MODE_PRIVATE)
    private val keyFollowedChannels = "key_followed_channels"

    private val _followedChannels = MutableStateFlow<List<ChannelData>>(emptyList())
    val followedChannels = _followedChannels.asStateFlow()

    init {
        loadFromDisk()
    }

    private fun loadFromDisk() {
        val jsonString = prefs.getString(keyFollowedChannels, null)
        if (!jsonString.isNullOrEmpty()) {
            try {
                val type = object : TypeToken<List<ChannelData>>() {}.type
                val savedList: List<ChannelData> = gson.fromJson(jsonString, type)
                _followedChannels.value = savedList
            } catch (e: Exception) {
                _followedChannels.value = emptyList()
            }
        }
    }

    fun updateFollowedChannels(newList: List<ChannelData>) {
        _followedChannels.value = newList
        saveToDisk(newList)
    }

    fun addChannel(channel: ChannelData) {
        val currentList = _followedChannels.value.toMutableList()
        if (currentList.none { it.id == channel.id }) {
            currentList.add(channel)
            updateFollowedChannels(currentList)
        }
    }

    fun removeChannel(channelId: Int) {
        val currentList = _followedChannels.value.toMutableList()
        val wasRemoved = currentList.removeAll { it.id == channelId }
        if (wasRemoved) {
            updateFollowedChannels(currentList)
        }
    }

    fun clear() {
        _followedChannels.value = emptyList()
        prefs.edit { clear() }
    }

    private fun saveToDisk(list: List<ChannelData>) {
        val jsonString = gson.toJson(list)
        prefs.edit {
            putString(keyFollowedChannels, jsonString)
        }
    }
}