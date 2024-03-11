package com.example.cert.domain.model


data class AnswerDomainDto(
    val answerId: Int,
    val content: String,
    var userAnswer: Boolean? = null
)
