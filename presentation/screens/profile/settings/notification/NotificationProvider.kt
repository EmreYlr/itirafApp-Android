package com.itirafapp.android.presentation.screens.profile.settings.notification

import com.itirafapp.android.domain.model.enums.NotificationEventType
import com.itirafapp.android.presentation.model.NotificationUiModel
import javax.inject.Inject

class NotificationMenuProvider @Inject constructor() {

    fun getMenu(): List<NotificationUiModel> {
        return listOf(
            NotificationUiModel(
                type = NotificationEventType.CONFESSION,
                title = "İtiraf",
                description = "İtiraflarından haberdar ol",
                isEnabled = true
            ),

            NotificationUiModel(
                type = NotificationEventType.MESSAGE,
                title = "Mesajlar",
                description = "Yeni bir mesaj aldığında haberdar ol",
                isEnabled = true
            ),

            NotificationUiModel(
                type = NotificationEventType.COMMENT,
                title = "Yorumlar",
                description = "İtiraflarına gelen yeni yorumları gör",
                isEnabled = true
            ),

            NotificationUiModel(
                type = NotificationEventType.LIKE,
                title = "Beğeniler",
                description = "İtirafların beğenildiğinde bildirim al",
                isEnabled = true
            ),

            NotificationUiModel(
                type = NotificationEventType.MESSAGE_REQUEST,
                title = "Mesaj İstekleri",
                description = "Sana gelen mesaj isteklerini gör",
                isEnabled = true
            ),

            NotificationUiModel(
                type = NotificationEventType.MESSAGE_REQUEST_RESULT,
                title = "Mesaj İstek Sonuçları",
                description = "Gönderdiğin isteklerin sonuçlarını gör",
                isEnabled = true
            ),

            NotificationUiModel(
                type = NotificationEventType.MODERATOR,
                title = "Moderatör",
                description = "İtiraflarının durum bilgilerininin bildirimini al",
                isEnabled = true
            )
        )
    }
}