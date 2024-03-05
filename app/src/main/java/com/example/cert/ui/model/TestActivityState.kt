package com.example.cert.ui.model

import com.example.cert.domain.model.QuestionsForTestingDomainDto


data class TestActivityState(
    var questions: QuestionsForTestingDomainDto? = null
)
