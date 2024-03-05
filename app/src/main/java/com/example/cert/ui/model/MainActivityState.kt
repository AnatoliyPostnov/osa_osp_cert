package com.example.cert.ui.model

import com.example.cert.domain.model.ExamsDomainDto

data class MainActivityState(
    var examsDomainDto: List<ExamsDomainDto> = emptyList(),
)
