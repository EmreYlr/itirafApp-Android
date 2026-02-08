package com.itirafapp.android.presentation.mapper

import com.itirafapp.android.domain.model.MessageData
import com.itirafapp.android.presentation.model.ChatUiItem
import com.itirafapp.android.util.extension.formatDateSeparator
import com.itirafapp.android.util.extension.formatToTime
import com.itirafapp.android.util.extension.parseToLocalDate

fun MessageData.toUiModel(showProfileImage: Boolean): ChatUiItem.MessageItem {
    val formattedTime = formatToTime(this.createdAt)
    return ChatUiItem.MessageItem(
        message = this.copy(createdAt = formattedTime),
        showProfileImage = showProfileImage
    )
}

fun List<MessageData>.toUiItemsWithDateSeparators(): List<ChatUiItem> {
    if (isEmpty()) return emptyList()

    val result = mutableListOf<ChatUiItem>()

    forEachIndexed { index, message ->
        val messageBelow = getOrNull(index - 1)
        val nextMessage = getOrNull(index + 1)

        val currentDate = parseToLocalDate(message.createdAt)
        val nextDate = nextMessage?.let { parseToLocalDate(it.createdAt) }
        val isDateChangeAfterThis = nextMessage != null &&
                currentDate != null && nextDate != null && currentDate != nextDate

        val showProfileImage = !message.isMyMessage &&
                (messageBelow == null || messageBelow.isMyMessage || isDateChangeAfterThis)

        result.add(message.toUiModel(showProfileImage = showProfileImage))

        val isLastMessage = index == size - 1
        if (isDateChangeAfterThis || isLastMessage) {
            val dateText = formatDateSeparator(message.createdAt)
            result.add(ChatUiItem.DateSeparator(dateText = dateText))
        }
    }

    return result
}