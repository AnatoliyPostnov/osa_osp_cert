package com.example.cert.domain.model

data class QuestionDomainDto(
    val questionPictureUri: String,
    val name: String,
    val questionId: Int,
    val type: String,
    val answers: List<AnswerDomainDto>
)
