package com.example.cert.domain.model

data class ExamTestResultRequestDto(
    val themeId: Int,
    val questions: List<QuestionDomainDto>
)
