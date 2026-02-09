package com.itirafapp.android.presentation.screens.terms

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material.icons.rounded.TheaterComedy
import com.itirafapp.android.R
import com.itirafapp.android.presentation.model.TermsItem
import com.itirafapp.android.util.state.UiText
import javax.inject.Inject

class TermsProvider @Inject constructor() {
    fun getTerms(): List<TermsItem> {
        return listOf(
            TermsItem(
                title = UiText.StringResource(R.string.terms_privacy_title),
                description = UiText.StringResource(R.string.terms_privacy_content),
                icon = Icons.Rounded.PrivacyTip
            ),
            TermsItem(
                title = UiText.StringResource(R.string.terms_service_title),
                description = UiText.StringResource(R.string.terms_service_content),
                icon = Icons.Rounded.AccountBalance
            ),
            TermsItem(
                title = UiText.StringResource(R.string.terms_conduct_title),
                description = UiText.StringResource(R.string.terms_conduct_content),
                icon = Icons.Rounded.TheaterComedy
            )
        )
    }
}