package com.example.cert.data.repository.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ResultAnswersDto(
    @JsonProperty("theme_id")
    val themeId: Int,
    @JsonProperty("theme_content")
    val themeContent: String,
    val questions: List<QuestionWithAnswerDto>
)

data class QuestionWithAnswerDto(
    @JsonProperty("question_id")
    val questionId: Int,
    @JsonProperty("correct_answers")
    val correctAnswers: List<Int>,
    val explanation: String
)
