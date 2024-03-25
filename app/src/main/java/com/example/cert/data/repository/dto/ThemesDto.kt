package com.example.cert.data.repository.dto

data class ThemesDto(
    val examId: Int,
    val themes: List<ThemeDto>
)

data class ThemeDto(
    val id: Long,
    val content: String
)