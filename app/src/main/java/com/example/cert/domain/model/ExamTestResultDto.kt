package com.example.cert.domain.model

data class ExamTestResultDto(
    val rightAnswers: Int,
    val wrongAnswers: Int,
    val correctAnswersPercentage: String,
    val questionsResult: List<QuestionResult>
)

data class QuestionResult(
    val id: Int,
    val isRight: Boolean,
    val yourAnswer: String,
    val rightAnswer: String,
    val explanation: String
)
