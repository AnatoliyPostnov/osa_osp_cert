package com.example.cert.ui.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.example.cert.domain.model.ExamTestResultDomainDto
import com.example.cert.domain.model.ExamTestResultRequestDomainDto
import com.example.cert.domain.model.QuestionsForTestingDomainDto


data class TestActivityState(
    // states
    val answerSelected: MutableMap<String, Boolean> = mutableStateMapOf(),
    val topItems: MutableMap<Int, TopItem> = mutableStateMapOf(),
    val resultItems: MutableMap<Int, ResultItem> = mutableStateMapOf(),
    val testResult: MutableState<TestResultState> = mutableStateOf(TestResultState(0, 0, "0")),
    val showResultState: MutableState<Boolean> = mutableStateOf(false),

    // domain
    val questions: QuestionsForTestingDomainDto? = null,
    val examTestResultRequestDomainDto: ExamTestResultRequestDomainDto? = null,
    val examTestResultDomainDto: ExamTestResultDomainDto? = null
)
