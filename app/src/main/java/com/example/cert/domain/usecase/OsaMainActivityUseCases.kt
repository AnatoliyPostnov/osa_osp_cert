package com.example.cert.domain.usecase

import android.content.Context
import com.example.cert.domain.model.ExamTestResultDto
import com.example.cert.domain.model.ExamsDomainDto
import com.example.cert.domain.model.QuestionsForTestingDomainDto
import com.example.cert.domain.model.ThemesDomainDto
import com.example.cert.domain.repository.OsaMainActivityRepository
import java.lang.RuntimeException
import javax.inject.Inject

class OsaMainActivityUseCases @Inject constructor(
    private val osaMainActivityRepository: OsaMainActivityRepository
) {

    fun getAllExams(context: Context): List<ExamsDomainDto> {
        return osaMainActivityRepository.getAllExams(context)
    }

    fun getAllThemesByExamId(context: Context, examId: Int?): ThemesDomainDto {
        if (examId == null) { return ThemesDomainDto(themes = listOf()) }
        return osaMainActivityRepository.getAllThemesByExamId(context, examId)
            ?: ThemesDomainDto(themes = listOf())
    }

    fun getQuestionsByThemeId(context: Context, themeId: Int?): QuestionsForTestingDomainDto {
        return themeId?.let { osaMainActivityRepository.getQuestionsByThemeId(context, themeId) }
            ?: throw RuntimeException("themeId can`t be null")
    }

    fun getOsaTestResult(examId: Int): ExamTestResultDto {
        TODO()
    }
}
