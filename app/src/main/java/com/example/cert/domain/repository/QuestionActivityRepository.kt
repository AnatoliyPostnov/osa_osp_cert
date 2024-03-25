package com.example.cert.domain.repository

import android.content.Context
import com.example.cert.domain.model.ExamTestResultDomainDto
import com.example.cert.domain.model.ExamTestResultRequestDomainDto
import com.example.cert.domain.model.ExamsDomainDto
import com.example.cert.domain.model.QuestionsForTestingDomainDto
import com.example.cert.domain.model.ThemesDomainDto

interface QuestionActivityRepository {
    fun getAllExams(context: Context): List<ExamsDomainDto>
    fun getAllThemesByExamId(context: Context, examId: Int): ThemesDomainDto?
    fun getQuestionsByThemeIdAndExamId(context: Context, themeId: Int, examId: Int): QuestionsForTestingDomainDto
    fun getQuestionsTestResult(context: Context, request: ExamTestResultRequestDomainDto): ExamTestResultDomainDto?
}
