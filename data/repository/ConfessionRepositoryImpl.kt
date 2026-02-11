package com.itirafapp.android.data.repository

import com.itirafapp.android.data.mapper.toDomain
import com.itirafapp.android.data.remote.confession.ConfessionService
import com.itirafapp.android.data.remote.confession.dto.ConfessionResponse
import com.itirafapp.android.data.remote.confession.dto.PostRequest
import com.itirafapp.android.data.remote.confession.dto.ReplyRequest
import com.itirafapp.android.data.remote.confession.dto.ReportConfessionRequest
import com.itirafapp.android.data.remote.confession.dto.ReportReplyRequest
import com.itirafapp.android.data.remote.confession.dto.ShortlinkRequest
import com.itirafapp.android.data.remote.confession.dto.ShortlinkResponse
import com.itirafapp.android.data.remote.confession.dto.ViewConfessionRequest
import com.itirafapp.android.data.remote.network.safeApiCall
import com.itirafapp.android.domain.model.ConfessionData
import com.itirafapp.android.domain.model.ConfessionDetail
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.domain.repository.ConfessionRepository
import com.itirafapp.android.util.state.Resource
import com.itirafapp.android.util.state.map
import javax.inject.Inject

class ConfessionRepositoryImpl @Inject constructor(
    private val api: ConfessionService
) : ConfessionRepository {
    override suspend fun getConfessions(
        page: Int,
        limit: Int
    ): Resource<PaginatedResult<ConfessionData>> {
        val apiResult: Resource<ConfessionResponse> = safeApiCall {
            api.fetchFlow(page = page, limit = limit)
        }

        return apiResult.map { response ->
            response.toDomain()
        }
    }

    override suspend fun getFollowingConfessions(
        page: Int,
        limit: Int
    ): Resource<PaginatedResult<ConfessionData>> {
        val apiResult: Resource<ConfessionResponse> = safeApiCall {
            api.fetchFollowingFlow(page = page, limit = limit)
        }

        return apiResult.map { response ->
            response.toDomain()
        }
    }

    override suspend fun getChannelConfession(
        id: Int,
        page: Int,
        limit: Int
    ): Resource<PaginatedResult<ConfessionData>> {
        val apiResult: Resource<ConfessionResponse> = safeApiCall {
            api.getChannelConfession(id, page = page, limit = limit)
        }

        return apiResult.map { response ->
            response.toDomain()
        }
    }

    override suspend fun postConfession(
        channelId: Int,
        title: String?,
        message: String
    ): Resource<Unit> {
        return safeApiCall {
            val request = PostRequest(title, message)
            api.postChannelConfession(channelId, request)
        }
    }

    override suspend fun postReply(
        id: Int,
        message: String
    ): Resource<Unit> {
        return safeApiCall {
            val request = ReplyRequest(message)
            api.repliesMessage(id, request)
        }
    }

    override suspend fun getConfessionDetail(id: Int): Resource<ConfessionDetail> {
        return safeApiCall {
            val result = api.fetchConfessionDetail(id)
            val detail = result.toDomain()
            detail
        }
    }

    override suspend fun createShortlink(id: Int): Resource<ShortlinkResponse> {
        return safeApiCall {
            val request = ShortlinkRequest(id)
            val shortlinkURL = api.repliesMessage(request)
            shortlinkURL
        }
    }

    override suspend fun likeConfession(id: Int): Resource<Unit> {
        return safeApiCall {
            api.likeConfession(id)
        }
    }

    override suspend fun unlikeConfession(id: Int): Resource<Unit> {
        return safeApiCall {
            api.unlikeConfession(id)
        }
    }

    override suspend fun deleteConfession(id: Int): Resource<Unit> {
        return safeApiCall {
            api.deleteConfession(id)
        }
    }

    override suspend fun editConfession(
        id: Int,
        title: String?,
        message: String
    ): Resource<Unit> {
        return safeApiCall {
            val request = PostRequest(title, message)
            api.editConfession(id, request)
        }
    }

    override suspend fun deleteReply(id: Int): Resource<Unit> {
        return safeApiCall {
            api.deleteReply(id)
        }
    }

    override suspend fun reportConfession(
        id: Int,
        reason: String
    ): Resource<Unit> {
        return safeApiCall {
            val request = ReportConfessionRequest(reason)
            api.reportConfession(id, request)
        }
    }

    override suspend fun reportReply(
        id: Int,
        reason: String
    ): Resource<Unit> {
        return safeApiCall {
            val request = ReportReplyRequest(reason)
            api.reportReply(id, request)
        }
    }

    override suspend fun viewConfession(ids: List<Int>): Resource<Unit> {
        return safeApiCall {
            val request = ViewConfessionRequest(ids)
            api.viewConfession(request)
        }
    }
}