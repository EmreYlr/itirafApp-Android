package com.itirafapp.android.domain.usecase.theme

import com.itirafapp.android.domain.repository.ThemeRepository
import com.itirafapp.android.util.constant.ThemeConfig
import javax.inject.Inject

class SetAppThemeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    suspend operator fun invoke(themeConfig: ThemeConfig) {
        themeRepository.setAppTheme(themeConfig)
    }
}