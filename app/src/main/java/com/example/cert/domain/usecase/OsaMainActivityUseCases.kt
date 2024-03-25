package com.example.cert.domain.usecase

import android.content.Context
import com.example.cert.domain.model.ExamTestResultDomainDto
import com.example.cert.domain.model.ExamTestResultRequestDomainDto
import com.example.cert.domain.model.ExamsDomainDto
import com.example.cert.domain.model.QuestionsForTestingDomainDto
import com.example.cert.domain.model.ThemesDomainDto
import com.example.cert.domain.repository.QuestionActivityRepository
import javax.inject.Inject
import kotlin.RuntimeException

class OsaMainActivityUseCases @Inject constructor(
    private val osaMainActivityRepository: QuestionActivityRepository
) {

    fun getAllExams(context: Context): List<ExamsDomainDto> {
        return osaMainActivityRepository.getAllExams(context)
    }

    fun getAllThemesByExamId(context: Context, examId: Int?): ThemesDomainDto {
        if (examId == null) { return ThemesDomainDto(themes = listOf(), examId = null) }
        return osaMainActivityRepository.getAllThemesByExamId(context, examId)
            ?: ThemesDomainDto(themes = listOf(), examId = null)
    }

    fun getQuestionsByThemeIdAndExamId(context: Context, themeId: Int?, examId: Int?): QuestionsForTestingDomainDto {
        if (themeId == null || examId == null) {
            throw RuntimeException("themeId or examId can`t be null")
        }
        return osaMainActivityRepository.getQuestionsByThemeIdAndExamId(context, themeId, examId)
    }

    fun getOsaTestResult(context: Context, request: ExamTestResultRequestDomainDto?): ExamTestResultDomainDto {
        if (request == null) { throw RuntimeException("request for getting result can`t be null") }
        return osaMainActivityRepository.getOsaTestResult(context, request)
            ?: throw RuntimeException("examTestResultDomainDto can`t be null")
    }
}
