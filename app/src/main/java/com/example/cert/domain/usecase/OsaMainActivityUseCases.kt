package com.example.cert.domain.usecase

import android.content.Context
import com.example.cert.domain.model.ExamTestResultDto
import com.example.cert.domain.model.ExamsDomainDto
import com.example.cert.domain.model.ThemesDomainDto
import com.example.cert.domain.repository.OsaMainActivityRepository
import javax.inject.Inject

class OsaMainActivityUseCases @Inject constructor(
    private val osaMainActivityRepository: OsaMainActivityRepository
) {

    fun getAllExams(context: Context): List<ExamsDomainDto> {
        return osaMainActivityRepository.getAllExams(context)
    }

    fun getAllThemesByExamId(examId: Long): ThemesDomainDto {
        TODO()
    }

    fun getQuestionsByThemeId(themeId: Long) {
        TODO()
    }

    fun getOsaTestResult(examId: Long): ExamTestResultDto {
        TODO()
    }
}