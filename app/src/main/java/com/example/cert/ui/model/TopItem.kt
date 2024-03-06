package com.example.cert.ui.model

import com.example.cert.domain.model.QuestionDomainDto


data class TopItem(
    val isAnswered: Boolean = false,
    val route: Int,
    val question: QuestionDomainDto
)
