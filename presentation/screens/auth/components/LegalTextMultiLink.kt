package com.itirafapp.android.presentation.screens.auth.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import com.itirafapp.android.R
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun LegalTextMultiLink(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fullTemplate = stringResource(R.string.auth_legal_full_text)
    val termsText = stringResource(R.string.terms_conditions_link)
    val privacyText = stringResource(R.string.privacy_policy_link)

    val linkStyle = TextLinkStyles(
        style = SpanStyle(
            color = ItirafTheme.colors.pureContrast,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline
        )
    )

    val annotatedString = buildAnnotatedString {
        val termsMarker = "||TERMS||"
        val privacyMarker = "||PRIVACY||"

        val rawString = fullTemplate.format(termsMarker, privacyMarker)

        val parts = rawString.split(termsMarker, privacyMarker)

        val firstMarkerIndex = rawString.indexOf(termsMarker)
        val secondMarkerIndex = rawString.indexOf(privacyMarker)

        val isTermsFirst = firstMarkerIndex < secondMarkerIndex

        if (parts.isNotEmpty()) append(parts[0])

        if (isTermsFirst) {
            withLink(LinkAnnotation.Clickable("terms", linkStyle, { onTermsClick() })) {
                append(termsText)
            }
            if (parts.size > 1) append(parts[1])

            withLink(LinkAnnotation.Clickable("privacy", linkStyle, { onPrivacyClick() })) {
                append(privacyText)
            }
        } else {
            withLink(LinkAnnotation.Clickable("privacy", linkStyle, { onPrivacyClick() })) {
                append(privacyText)
            }
            if (parts.size > 1) append(parts[1])

            withLink(LinkAnnotation.Clickable("terms", linkStyle, { onTermsClick() })) {
                append(termsText)
            }
        }

        if (parts.size > 2) append(parts[2])
    }

    Text(
        text = annotatedString,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            lineHeight = MaterialTheme.typography.bodySmall.fontSize * 1.5
        )
    )
}