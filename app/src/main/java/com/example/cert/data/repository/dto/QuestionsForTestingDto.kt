package com.example.cert.data.repository.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class QuestionsForTestingDto(
    @JsonProperty("theme_id")
    val themeId: Int,
    @JsonProperty("theme_content")
    val themeContent: String,
    val questions: List<QuestionDto>
)

data class QuestionDto(
    @JsonProperty("question_picture_uri")
    val questionPictureUri: String,
    val name: String,
    @JsonProperty("question_id")
    val questionId: Int,
    val type: String,
    val answers: List<AnswerDto>
)

data class AnswerDto(
    val answerId: Int,
    val content: String
)
