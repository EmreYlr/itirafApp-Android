package com.itirafapp.android.presentation.screens.auth.components

import androidx.annotation.StringRes
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun TermText(
    @StringRes fullTextRes: Int,
    @StringRes linkTextRes: Int,
    onLinkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fullPattern = stringResource(id = fullTextRes)
    val linkText = stringResource(id = linkTextRes)

    val finalString = fullPattern.format(linkText)

    val startIndex = finalString.indexOf(linkText)
    val endIndex = startIndex + linkText.length

    val annotatedString = buildAnnotatedString {
        if (startIndex > 0) {
            append(finalString.take(startIndex))
        }

        val link = LinkAnnotation.Clickable(
            tag = "terms_link",
            styles = TextLinkStyles(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            ),
            linkInteractionListener = {
                onLinkClick()
            }
        )

        withLink(link) {
            append(linkText)
        }

        if (endIndex < finalString.length) {
            append(finalString.substring(endIndex))
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = ItirafTheme.colors.textSecondary
        )
    )
}
