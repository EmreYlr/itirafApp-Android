package com.itirafapp.android.domain.usecase.theme

import com.itirafapp.android.domain.repository.ThemeRepository
import com.itirafapp.android.util.constant.ThemeConfig
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppThemeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    operator fun invoke(): Flow<ThemeConfig> {
        return themeRepository.getAppTheme()
    }
}