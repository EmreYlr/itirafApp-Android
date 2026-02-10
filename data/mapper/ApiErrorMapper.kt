package com.itirafapp.android.data.mapper

import com.itirafapp.android.R
import com.itirafapp.android.util.state.UiText

object ApiErrorMapper {

    fun getTitle(code: Int): UiText {
        return when (code) {
            // Server Errors
            1000, 1001, 4000, 4001, 1200, 4200, 1500, 4500 -> UiText.StringResource(R.string.error_title_server)

            // Auth Errors
            1400, 4400, 1401, 4401, 1402, 4402 -> UiText.StringResource(R.string.error_title_session)
            1403, 4403 -> UiText.StringResource(R.string.error_title_access_denied)

            // Validation
            in 1100..1199, in 4100..4199 -> UiText.StringResource(R.string.error_title_validation)

            // Default
            else -> UiText.StringResource(R.string.error_title_generic)
        }
    }

    fun getMessage(code: Int): UiText {
        return when (code) {
            // INTERNAL / SERVER
            1000, 1001, 1200, 1500, 1503, 1504, 1601, 1602,
            4000, 4001, 4200, 4500, 4503, 4504, 4601, 4602 -> UiText.StringResource(R.string.error_message_server)

            // CLIENT / BAD REQUEST
            1002, 4002 -> UiText.StringResource(R.string.error_message_bad_request)
            1003, 4003 -> UiText.StringResource(R.string.error_message_too_many_requests)

            // VALIDATION
            1100, 4100 -> UiText.StringResource(R.string.error_message_validation)
            1101, 4101 -> UiText.StringResource(R.string.error_message_invalid_input)
            1102, 4102 -> UiText.StringResource(R.string.error_message_missing_fields)
            in 1100..1199, in 4100..4199 -> UiText.StringResource(R.string.error_message_check_input)

            // DATABASE / RESOURCE
            1201, 4201 -> UiText.StringResource(R.string.error_message_record_exists)
            1300, 4300 -> UiText.StringResource(R.string.error_message_not_found)
            1301, 4301 -> UiText.StringResource(R.string.error_message_already_exists)
            1302, 4302 -> UiText.StringResource(R.string.error_message_conflict)

            // AUTHENTICATION
            1400, 4400 -> UiText.StringResource(R.string.error_message_unauthorized)
            1401, 4401 -> UiText.StringResource(R.string.error_message_session_invalid)
            1402, 4402 -> UiText.StringResource(R.string.error_message_session_expired)
            1403, 4403 -> UiText.StringResource(R.string.error_message_access_denied)
            1404, 4404 -> UiText.StringResource(R.string.error_message_auth_failed)
            1405, 4405 -> UiText.StringResource(R.string.error_message_account_not_verified)
            1406, 4406 -> UiText.StringResource(R.string.error_message_invalid_provider)
            1407, 4407 -> UiText.StringResource(R.string.error_message_invalid_token)

            // EXTERNAL
            1501, 4501 -> UiText.StringResource(R.string.error_message_timeout)

            // BUSINESS LOGIC
            1600, 4600 -> UiText.StringResource(R.string.error_message_process_failed)

            // WEBSOCKET
            in 1700..1999, in 4700..4999 -> UiText.StringResource(R.string.error_message_connection_failed)

            // DEFAULT
            else -> UiText.StringResource(R.string.error_message_unknown)
        }
    }
}