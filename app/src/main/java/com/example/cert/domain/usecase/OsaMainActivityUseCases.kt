package com.example.cert.domain.usecase

import com.example.cert.domain.model.ExamTestResultDto
import com.example.cert.domain.model.ExamsDomainDto
import com.example.cert.domain.model.ThemesDomainDto
import com.example.cert.domain.repository.OsaMainActivityRepository

class OsaMainActivityUseCases(
    private val osaMainActivityRepository: OsaMainActivityRepository
) {

    fun getAllExams(): List<ExamsDomainDto> {
        return osaMainActivityRepository.getAllExams()
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