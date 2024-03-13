package com.example.cert.ui.model

import androidx.compose.ui.graphics.Color
import com.example.cert.domain.model.QuestionDomainDto


data class TopItem(
    var isCommitted: Boolean = false,
    var color: Color = Color.LightGray,
    val route: Int,
    val question: QuestionDomainDto
)
