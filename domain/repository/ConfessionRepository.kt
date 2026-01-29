package com.itirafapp.android.domain.repository

import com.itirafapp.android.data.remote.confession.dto.ShortlinkResponse
import com.itirafapp.android.domain.model.ConfessionData
import com.itirafapp.android.domain.model.ConfessionDetail
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.util.state.Resource

interface ConfessionRepository {
    suspend fun getConfessions(page: Int, limit: Int): Resource<PaginatedResult<ConfessionData>>
    suspend fun getFollowingConfessions(
        page: Int,
        limit: Int
    ): Resource<PaginatedResult<ConfessionData>>

    suspend fun getChannelConfession(
        id: Int,
        page: Int,
        limit: Int
    ): Resource<PaginatedResult<ConfessionData>>
    suspend fun postConfession(channelId: Int, title: String?, message: String): Resource<Unit>
    suspend fun postReply(id: Int, message: String): Resource<Unit>
    suspend fun getConfessionDetail(id: Int): Resource<ConfessionDetail>
    suspend fun createShortlink(id: Int): Resource<ShortlinkResponse>
    suspend fun likeConfession(id: Int): Resource<Unit>
    suspend fun unlikeConfession(id: Int): Resource<Unit>
    suspend fun deleteConfession(id: Int): Resource<Unit>
    suspend fun editConfession(id: Int, title: String?, message: String): Resource<Unit>
    suspend fun deleteReply(id: Int): Resource<Unit>
    suspend fun reportConfession(id: Int, reason: String): Resource<Unit>
    suspend fun reportReply(id: Int, reason: String): Resource<Unit>
}