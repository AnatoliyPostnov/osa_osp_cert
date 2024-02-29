package com.example.cert.domain.model

data class ExamTestResultRequestDto(
    val themeId: Int,
    val questions: List<QuestionDto>
)

data class QuestionDto(
    val questionInt: Int,
    val type: String,
    val answers: List<AnswerDto>
)

data class AnswerDto(
    val answerId: Int,
    val userAnswer: Boolean
)