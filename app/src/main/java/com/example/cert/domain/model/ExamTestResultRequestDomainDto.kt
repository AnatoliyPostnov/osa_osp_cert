package com.example.cert.domain.model

data class ExamTestResultRequestDomainDto(
    val themeId: Int,
    val examId: Int,
    val questions: List<QuestionDomainDto>
)
