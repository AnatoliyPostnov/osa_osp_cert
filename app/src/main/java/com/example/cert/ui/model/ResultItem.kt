package com.example.cert.ui.model

import com.example.cert.domain.model.AnswerDomainDto


data class ResultItem(
    val id: Int,
    val isRight: Boolean,
    val yourAnswer: String,
    val rightAnswer: String,
    val explanation: String,
    val question: String,
    val answers: List<AnswerDomainDto>
)
