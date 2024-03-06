package com.example.cert.domain.model

data class QuestionDomainDto(
    val content: String,
    val questionId: Int,
    val type: String,
    val answers: List<AnswerDomainDto>
)
