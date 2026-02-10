package com.itirafapp.android.util.extension

import com.itirafapp.android.R
import com.itirafapp.android.domain.model.AppError
import com.itirafapp.android.util.state.UiText

fun AppError.refinedForLogin(): AppError {
    if (this is AppError.ApiError) {
        return when (code) {
            1400 -> this.copy(
                userMessage = UiText.StringResource(R.string.login_credentials_error)
            )
            1409 -> this.copy(
                userMessage = UiText.StringResource(R.string.register_email_deleted)
            )

            else -> this
        }
    }
    return this
}

fun AppError.refinedForRegister(): AppError {
    if (this is AppError.ApiError) {
        return when (code) {
            1302 -> this.copy(
                userMessage = UiText.StringResource(R.string.register_conflict)
            )
            1409 -> this.copy(
                userMessage = UiText.StringResource(R.string.register_email_deleted)
            )

            else -> this
        }
    }
    return this
}

fun AppError.refinedForBusiness(): AppError {
    if (this is AppError.ApiError) {
        return when (code) {
            1302 -> this.copy(
                userMessage = UiText.StringResource(R.string.conflict_business)
            )

            else -> this
        }
    }
    return this
}