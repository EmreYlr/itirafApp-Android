package com.itirafapp.android.presentation.screens.onboarding

import android.R
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itirafapp.android.presentation.model.OnboardingPage
import com.itirafapp.android.presentation.screens.onboarding.components.BottomSection
import com.itirafapp.android.presentation.screens.onboarding.components.OnboardingSlide
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.util.state.UiText

@Composable
fun OnboardingScreen(
    onboardingCompleted: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { onboardingUiEvent ->
            when (onboardingUiEvent) {
                is OnboardingUiEvent.NavigateToTerms -> {
                    onboardingCompleted()
                }
            }
        }
    }

    OnboardingContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingContent(
    state: OnboardingState,
    onEvent: (OnboardingEvent) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { state.pages.size })

    LaunchedEffect(state.currentPageIndex) {
        pagerState.animateScrollToPage(state.currentPageIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        if (state.currentPageIndex != pagerState.currentPage) {
            onEvent(OnboardingEvent.UpdatePage(pagerState.currentPage))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {

        OnboardingSlide(
            modifier = Modifier.weight(1f),
            pagerState = pagerState,
            pages = state.pages
        )

        BottomSection(
            isFirstPage = state.currentPageIndex == 0,
            isLastPage = state.currentPageIndex == state.pages.size - 1,
            onBackClick = { onEvent(OnboardingEvent.ClickBack) },
            onNextClick = { onEvent(OnboardingEvent.ClickNext) }
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OnboardingContentPreview_FirstPage() {
    val mockPages = listOf(
        OnboardingPage(
            title = UiText.DynamicString("Hoş Geldiniz"),
            description = UiText.DynamicString("İtiraf uygulamasına hoş geldiniz, içinizi dökün."),
            imageRes = R.drawable.ic_menu_gallery
        ),
        OnboardingPage(
            title = UiText.DynamicString("Gizlilik"),
            description = UiText.DynamicString("Kimliğiniz tamamen anonim kalacaktır."),
            imageRes = R.drawable.ic_menu_view
        ),
        OnboardingPage(
            title = UiText.DynamicString("Başlayın"),
            description = UiText.DynamicString("Hemen sohbete başlayın."),
            imageRes = R.drawable.ic_menu_compass
        )
    )

    ItirafAppTheme() {
        OnboardingContent(
            state = OnboardingState(
                currentPageIndex = 1,
                pages = mockPages
            ),
            onEvent = {}
        )
    }
}