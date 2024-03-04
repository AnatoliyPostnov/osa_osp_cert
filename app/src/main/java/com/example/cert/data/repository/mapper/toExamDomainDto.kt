package com.example.cert.data.repository.mapper

import com.example.cert.data.repository.dto.ExamsDto
import com.example.cert.data.repository.dto.ThemesDto
import com.example.cert.domain.model.ExamsDomainDto
import com.example.cert.domain.model.ThemeDomainDto
import com.example.cert.domain.model.ThemesDomainDto

fun toExamDomainDto(examsDto: ExamsDto?): ExamsDomainDto? {
    return examsDto?.let {
        ExamsDomainDto(
            examId = it.examId,
            content = it.content,
            pictureFileName = it.pictureFileName
        )
    }
}

fun toThemesDomainDto(themesDto: ThemesDto?): ThemesDomainDto? {
    return themesDto?.let {
        ThemesDomainDto(
            themes = themesDto
                .themes
                .map {
                    ThemeDomainDto(
                        id = it.id,
                        content = it.content
                    )
                }
        )
    }
}
