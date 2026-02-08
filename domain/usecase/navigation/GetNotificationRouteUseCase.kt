package com.itirafapp.android.domain.usecase.navigation

import com.itirafapp.android.domain.model.NotificationData
import com.itirafapp.android.domain.model.NotificationEventStatus
import com.itirafapp.android.domain.model.NotificationEventType
import com.itirafapp.android.domain.model.NotificationItem
import com.itirafapp.android.presentation.navigation.Screen
import javax.inject.Inject

class GetNotificationRouteUseCase @Inject constructor() {

    operator fun invoke(item: NotificationItem): String? {
        return resolveRoute(item.eventType, item.data)
    }

    operator fun invoke(fcmData: Map<String, String>): String? {
        val eventTypeStr = fcmData["eventType"] ?: return null

        val eventType = try {
            NotificationEventType.valueOf(eventTypeStr)
        } catch (e: Exception) {
            return null
        }

        val data = NotificationData(
            roomId = fcmData["roomId"],
            requestId = fcmData["requestId"],
            senderName = fcmData["senderName"],
            senderId = fcmData["senderId"],
            messageId = fcmData["messageId"],
            commentId = fcmData["commentId"],
            status = try {
                NotificationEventStatus.valueOf(fcmData["status"] ?: "")
            } catch (e: Exception) {
                NotificationEventStatus.UNKNOWN
            },
            notificationId = null
        )

        return resolveRoute(eventType, data)
    }

    private fun resolveRoute(eventType: NotificationEventType, data: NotificationData): String? {
        return when (eventType) {
            NotificationEventType.MESSAGE -> {
                val roomId = data.roomId ?: return null
                val title = data.senderName ?: "Chat"
                Screen.Chat.createRoute(roomId, title)
            }

            NotificationEventType.LIKE, NotificationEventType.COMMENT -> {
                val messageId = data.messageId ?: return null
                Screen.Detail.createRoute(messageId)
            }

            NotificationEventType.MESSAGE_REQUEST -> {
                Screen.Message.createRoute(tabIndex = 1)
            }

            NotificationEventType.MESSAGE_REQUEST_RESULT -> {
                if (data.status == NotificationEventStatus.ACCEPTED) {
                    val roomId = data.roomId ?: return null
                    val title = data.senderName ?: "Chat"
                    Screen.Chat.createRoute(roomId, title)
                } else {
                    Screen.SentMessage.route
                }
            }

            NotificationEventType.MODERATOR -> {
                Screen.MyConfession.route
            }

            NotificationEventType.CONFESSION -> {
                Screen.Home.route
            }

            else -> null
        }
    }
}