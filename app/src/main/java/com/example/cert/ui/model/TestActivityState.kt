package com.example.cert.ui.model

import androidx.compose.runtime.mutableStateMapOf
import com.example.cert.domain.model.QuestionsForTestingDomainDto


data class TestActivityState(
    val questions: QuestionsForTestingDomainDto? = null,
    val answerSelected: MutableMap<String, Boolean> = mutableStateMapOf()
)
