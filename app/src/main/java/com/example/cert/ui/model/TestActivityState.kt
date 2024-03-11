package com.example.cert.ui.model

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.cert.domain.model.QuestionsForTestingDomainDto


data class TestActivityState(
    val questions: QuestionsForTestingDomainDto? = null,
    val answerIcons: MutableMap<String, ImageVector> = mutableStateMapOf()
)
