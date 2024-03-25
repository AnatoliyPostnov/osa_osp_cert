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

class QuestionActivityUseCases @Inject constructor(
    private val questionActivityRepository: QuestionActivityRepository
) {

    fun getAllExams(context: Context): List<ExamsDomainDto> {
        return questionActivityRepository.getAllExams(context)
    }

    fun getAllThemesByExamId(context: Context, examId: Int?): ThemesDomainDto {
        if (examId == null) { return ThemesDomainDto(themes = listOf(), examId = null) }
        return questionActivityRepository.getAllThemesByExamId(context, examId)
            ?: ThemesDomainDto(themes = listOf(), examId = null)
    }

    fun getQuestionsByThemeIdAndExamId(context: Context, themeId: Int?, examId: Int?): QuestionsForTestingDomainDto {
        if (themeId == null || examId == null) {
            throw RuntimeException("themeId or examId can`t be null")
        }
        return questionActivityRepository.getQuestionsByThemeIdAndExamId(context, themeId, examId)
    }

    fun getQuestionsTestResult(context: Context, request: ExamTestResultRequestDomainDto?): ExamTestResultDomainDto {
        if (request == null) { throw RuntimeException("request for getting result can`t be null") }
        return questionActivityRepository.getQuestionsTestResult(context, request)
            ?: throw RuntimeException("examTestResultDomainDto can`t be null")
    }
}
