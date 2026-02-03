package com.itirafapp.android.presentation.screens.profile.settings.notification

import com.itirafapp.android.R
import com.itirafapp.android.domain.model.NotificationEventType
import com.itirafapp.android.presentation.model.NotificationUiModel
import com.itirafapp.android.util.state.UiText
import javax.inject.Inject

class NotificationMenuProvider @Inject constructor() {

    fun getMenu(): List<NotificationUiModel> {
        return listOf(
            NotificationUiModel(
                type = NotificationEventType.CONFESSION,
                title = UiText.StringResource(R.string.notification_confession_title),
                description = UiText.StringResource(R.string.notification_confession_desc),
                isEnabled = true
            ),

            NotificationUiModel(
                type = NotificationEventType.MESSAGE,
                title = UiText.StringResource(R.string.notification_message_title),
                description = UiText.StringResource(R.string.notification_message_desc),
                isEnabled = true
            ),

            NotificationUiModel(
                type = NotificationEventType.COMMENT,
                title = UiText.StringResource(R.string.notification_comment_title),
                description = UiText.StringResource(R.string.notification_comment_desc),
                isEnabled = true
            ),

            NotificationUiModel(
                type = NotificationEventType.LIKE,
                title = UiText.StringResource(R.string.notification_like_title),
                description = UiText.StringResource(R.string.notification_like_desc),
                isEnabled = true
            ),

            NotificationUiModel(
                type = NotificationEventType.MESSAGE_REQUEST,
                title = UiText.StringResource(R.string.notification_request_title),
                description = UiText.StringResource(R.string.notification_request_desc),
                isEnabled = true
            ),

            NotificationUiModel(
                type = NotificationEventType.MESSAGE_REQUEST_RESULT,
                title = UiText.StringResource(R.string.notification_request_result_title),
                description = UiText.StringResource(R.string.notification_request_result_desc),
                isEnabled = true
            ),

            NotificationUiModel(
                type = NotificationEventType.MODERATOR,
                title = UiText.StringResource(R.string.notification_moderator_title),
                description = UiText.StringResource(R.string.notification_moderator_desc),
                isEnabled = true
            )
        )
    }
}