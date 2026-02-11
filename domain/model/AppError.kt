package com.itirafapp.android.domain.model

import com.itirafapp.android.R
import com.itirafapp.android.data.mapper.ApiErrorMapper
import com.itirafapp.android.util.state.UiText

sealed interface AppError {
    val code: Int?
    val title: UiText
    val message: UiText

    data class ApiError(
        override val code: Int,
        val type: String?,
        val serverDebugMessage: String? = null,
        val userMessage: UiText? = null,
        val userTitle: UiText? = null
    ) : AppError {

        override val title: UiText
            get() = userTitle ?: ApiErrorMapper.getTitle(code)

        override val message: UiText
            get() = userMessage ?: ApiErrorMapper.getMessage(code)
    }

    sealed interface LocalError : AppError {
        override val code: Int? get() = null

        data object NoInternet : LocalError {
            override val title = UiText.StringResource(R.string.error_title_no_internet)
            override val message = UiText.StringResource(R.string.error_message_no_internet)
        }

        data object Unknown : LocalError {
            override val title = UiText.StringResource(R.string.error_title_unknown)
            override val message = UiText.StringResource(R.string.error_message_unknown)
        }
    }

    sealed interface ValidationError : AppError {
        override val code: Int? get() = null
        override val title: UiText
            get() = UiText.StringResource(R.string.validation_title_missing_info)

        data object InvalidEmail : ValidationError {
            override val message = UiText.StringResource(R.string.validation_error_invalid_email)
        }

        data class PasswordTooShort(val min: Int) : ValidationError {
            override val message = UiText.StringResource(
                resId = R.string.validation_error_password_short,
                args = arrayOf(min)
            )
        }

        data class EmptyField(val fieldName: UiText) : ValidationError {
            override val message = UiText.StringResource(
                resId = R.string.validation_error_field_empty,
                args = arrayOf(fieldName)
            )
        }
    }

    sealed interface BusinessError : AppError {
        override val code: Int? get() = null

        override val title: UiText
            get() = UiText.StringResource(R.string.business_error_title_failed)

        data object ChannelIdNotFound : BusinessError {
            override val message = UiText.StringResource(R.string.business_error_channel_not_found)
        }

        data object InvalidConfessionData : BusinessError {
            override val message = UiText.StringResource(R.string.business_error_invalid_data)
        }

        data object RequestSentIdNotFound : BusinessError {
            override val message =
                UiText.StringResource(R.string.business_error_request_id_not_found)
        }
    }

    sealed interface AuthError : AppError {
        override val code: Int? get() = null

        data object AnonymousUser : AuthError {
            override val title = UiText.StringResource(R.string.must_login_title)
            override val message = UiText.StringResource(R.string.must_login_description)
        }
    }
}

val AppError.shouldShowToast: Boolean
    get() = this !is AppError.AuthError.AnonymousUser