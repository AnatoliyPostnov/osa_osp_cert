package com.example.cert.domain.model


data class QuestionsForTestingDomainDto(
    val examId: Int,
    val themeId: Int,
    val themeContent: String,
    var questions: List<QuestionDomainDto>
)
