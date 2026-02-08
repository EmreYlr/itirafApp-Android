package com.itirafapp.android.util.manager

import android.util.Log
import com.google.gson.Gson
import com.itirafapp.android.data.remote.auth.dto.RefreshTokenRequest
import com.itirafapp.android.data.remote.network.api.TokenRefreshApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ChatWebSocketManager @Inject constructor(
    @param:Named("WebSocketOkHttpClient") private val client: OkHttpClient,
    @param:Named("WebSocketUrl") private val wsUrl: String,
    private val tokenManager: TokenManager,
    private val gson: Gson,
    private val tokenRefreshApi: TokenRefreshApi
) : WebSocketListener() {

    private var webSocket: WebSocket? = null
    private var currentRoomId: String? = null
    private var lastAttemptedMessage: String? = null //Birden fazla mesaj gelme durumu

    private val _incomingMessages = MutableSharedFlow<String>(replay = 0)
    val incomingMessages = _incomingMessages.asSharedFlow()

    private val _connectionState =
        MutableStateFlow<SocketConnectionState>(SocketConnectionState.Disconnected)
    val connectionState = _connectionState.asStateFlow()
    private val messageQueue = ConcurrentLinkedQueue<String>()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun connect(roomId: String) {
        if (currentRoomId == roomId && _connectionState.value is SocketConnectionState.Connected) return

        disconnect()
        currentRoomId = roomId

        scope.launch {
            startConnection(roomId)
        }
    }

    fun disconnect() {
        webSocket?.close(1000, "User Left")
        webSocket = null
        _connectionState.value = SocketConnectionState.Disconnected
    }

    fun sendMessage(messageContent: String) {
        val dataMap = mapOf(
            "content" to messageContent
        )

        val messageMap = mapOf(
            "type" to "message",
            "data" to dataMap
        )

        val jsonMessage = gson.toJson(messageMap)

        lastAttemptedMessage = jsonMessage

        if (_connectionState.value is SocketConnectionState.Connected) {
            val sent = webSocket?.send(jsonMessage) ?: false
            if (!sent) {
                messageQueue.add(jsonMessage)
                lastAttemptedMessage = null
            }
        } else {
            messageQueue.add(jsonMessage)
            lastAttemptedMessage = null
        }
    }

    private suspend fun startConnection(roomId: String) {
        _connectionState.emit(SocketConnectionState.Connecting)

        val token = tokenManager.getAccessToken()
        if (token.isNullOrEmpty()) {
            _connectionState.emit(SocketConnectionState.Error("Token bulunamadı"))
            return
        }

        val request = Request.Builder()
            .url("${wsUrl}chat/$roomId")
            .addHeader("Authorization", "Bearer $token")
            .build()

        webSocket = client.newWebSocket(request, this)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        scope.launch {
            _connectionState.emit(SocketConnectionState.Connected)

            sendSeenRequest(webSocket)

            while (messageQueue.isNotEmpty()) {
                val msg = messageQueue.poll()
                msg?.let {
                    webSocket.send(it)
                }
            }
        }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        scope.launch {
            _incomingMessages.emit(text)
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.w("SocketManager", "Kapanıyor: $code")
        if (code == 4402 || code == 401) {
            lastAttemptedMessage?.let { lostMessage ->
                Log.d(
                    "SocketManager",
                    "Token hatası yüzünden gitmeyen mesaj kurtarıldı ve kuyruğa eklendi."
                )
                messageQueue.add(lostMessage)
                lastAttemptedMessage = null
            }

            scope.launch { handleExpiredToken() }
        } else {
            webSocket.close(1000, null)
            _connectionState.value = SocketConnectionState.Disconnected
            lastAttemptedMessage = null
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        scope.launch {
            if (response?.code == 401) {
                lastAttemptedMessage?.let { lostMessage ->
                    messageQueue.add(lostMessage)
                    lastAttemptedMessage = null
                }
                handleExpiredToken()
            } else {
                _connectionState.emit(
                    SocketConnectionState.Error(
                        t.localizedMessage ?: "Connection Error"
                    )
                )
            }
        }
    }

    private fun sendSeenRequest(socket: WebSocket) {
        val seenObj = mapOf("type" to "seen")
        socket.send(gson.toJson(seenObj))
    }

    private suspend fun handleExpiredToken() {
        val refreshToken = tokenManager.getRefreshToken() ?: return

        try {
            val response = tokenRefreshApi.refreshToken(RefreshTokenRequest(refreshToken)).execute()

            if (response.isSuccessful && response.body() != null) {
                val tokens = response.body()!!
                tokenManager.saveTokens(tokens.accessToken, tokens.refreshToken)

                webSocket?.close(1000, "Refreshing Token")
                webSocket = null

                currentRoomId?.let { roomId ->
                    delay(500)
                    startConnection(roomId)
                }
            } else {
                _connectionState.emit(SocketConnectionState.Error("Session Expired"))
            }
        } catch (e: Exception) {
            _connectionState.emit(SocketConnectionState.Error("Auth Error"))
        }
    }
}

sealed class SocketConnectionState {
    object Disconnected : SocketConnectionState()
    object Connecting : SocketConnectionState()
    object Connected : SocketConnectionState()
    data class Error(val message: String) : SocketConnectionState()
}