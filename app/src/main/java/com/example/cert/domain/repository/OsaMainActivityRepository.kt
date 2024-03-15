package com.example.cert.domain.repository

import android.content.Context
import com.example.cert.domain.model.ExamTestResultDomainDto
import com.example.cert.domain.model.ExamTestResultRequestDomainDto
import com.example.cert.domain.model.ExamsDomainDto
import com.example.cert.domain.model.QuestionsForTestingDomainDto
import com.example.cert.domain.model.ThemesDomainDto

interface OsaMainActivityRepository {
    fun getAllExams(context: Context): List<ExamsDomainDto>
    fun getAllThemesByExamId(context: Context, examId: Int): ThemesDomainDto?
    fun getQuestionsByThemeId(context: Context, themeId: Int): QuestionsForTestingDomainDto?
    fun getOsaTestResult(context: Context, request: ExamTestResultRequestDomainDto): ExamTestResultDomainDto?
}
