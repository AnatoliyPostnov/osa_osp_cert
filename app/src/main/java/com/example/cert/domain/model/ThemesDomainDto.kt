package com.example.cert.domain.model

data class ThemesDomainDto(
    val themes: List<ThemeDomainDto>
)

data class ThemeDomainDto(
    val id: Long,
    val content: String
)