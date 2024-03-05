package com.example.cert.ui.model

import com.example.cert.domain.model.ThemeDomainDto

data class ThemesActivityState(
    var themes: List<ThemeDomainDto> = emptyList()
)
