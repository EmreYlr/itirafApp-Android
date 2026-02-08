package com.itirafapp.android.domain.usecase.navigation

import com.itirafapp.android.data.mapper.mapDomainEventToApi
import com.itirafapp.android.domain.model.NotificationData
import com.itirafapp.android.domain.model.NotificationEventStatus
import com.itirafapp.android.domain.model.NotificationItem
import com.itirafapp.android.domain.model.enums.NotificationApiEventType
import com.itirafapp.android.presentation.navigation.Screen
import javax.inject.Inject

class GetNotificationRouteUseCase @Inject constructor() {

    operator fun invoke(item: NotificationItem): String? {
        return resolveRoute(mapDomainEventToApi(item.eventType), item.data)
    }

    operator fun invoke(fcmData: Map<String, String>): String? {
        val eventTypeStr = fcmData["type"] ?: return null

        val eventType = try {
            NotificationApiEventType.valueOf(eventTypeStr)
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

    private fun resolveRoute(eventType: NotificationApiEventType, data: NotificationData): String? {
        return when (eventType) {
            NotificationApiEventType.DM_RECEIVED -> {
                val roomId = data.roomId ?: return null
                val title = data.senderName ?: "Chat"
                Screen.Chat.createRoute(roomId, title)
            }

            NotificationApiEventType.CONFESSION_LIKED, NotificationApiEventType.CONFESSION_REPLIED -> {
                val messageId = data.messageId ?: return null
                Screen.Detail.createRoute(messageId)
            }

            NotificationApiEventType.DM_REQUEST_RECEIVED -> {
                Screen.Message.createRoute(tabIndex = 1)
            }

            NotificationApiEventType.DM_REQUEST_RESPONDED -> {
                if (data.status == NotificationEventStatus.ACCEPTED) {
                    val roomId = data.roomId ?: return null
                    val title = data.senderName ?: "Chat"
                    Screen.Chat.createRoute(roomId, title)
                } else {
                    Screen.SentMessage.route
                }
            }

            NotificationApiEventType.CONFESSION_MODERATED -> {
                Screen.MyConfession.route
            }

            NotificationApiEventType.CONFESSION_PUBLISHED -> {
                Screen.Home.route
            }

            else -> null
        }
    }
}