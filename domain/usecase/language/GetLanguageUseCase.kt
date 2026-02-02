package com.itirafapp.android.domain.usecase.language

import com.itirafapp.android.domain.repository.LanguageRepository
import com.itirafapp.android.util.constant.LanguageConfig
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val repo: LanguageRepository
) {
    operator fun invoke(): LanguageConfig {
        return repo.getCurrentLanguage()
    }
}