package com.itirafapp.android.domain.model.enums

import com.google.gson.annotations.SerializedName

enum class NotificationChannelType {
    @SerializedName("EMAIL")
    EMAIL,
    @SerializedName("PUSH")
    PUSH
}

enum class NotificationApiEventType {
    @SerializedName("CONFESSION_REPLIED")
    CONFESSION_REPLIED,
    @SerializedName("CONFESSION_PUBLISHED")
    CONFESSION_PUBLISHED,
    @SerializedName("CONFESSION_LIKED")
    CONFESSION_LIKED,
    @SerializedName("DM_RECEIVED")
    DM_RECEIVED,
    @SerializedName("DM_REQUEST_RECEIVED")
    DM_REQUEST_RECEIVED,
    @SerializedName("DM_REQUEST_RESPONDED")
    DM_REQUEST_RESPONDED,
    @SerializedName("CONFESSION_MODERATED")
    CONFESSION_MODERATED
}