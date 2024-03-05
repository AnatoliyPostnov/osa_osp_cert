package com.example.cert.data.repository

import android.content.Context
import com.example.cert.data.repository.itrf.BackendCommunicationService
import com.example.cert.data.repository.mapper.toExamDomainDto
import com.example.cert.data.repository.mapper.toQuestionsForTestingDomainDto
import com.example.cert.data.repository.mapper.toThemesDomainDto
import com.example.cert.domain.model.ExamsDomainDto
import com.example.cert.domain.model.QuestionsForTestingDomainDto
import com.example.cert.domain.model.ThemesDomainDto
import com.example.cert.domain.repository.OsaMainActivityRepository
import javax.inject.Inject

class OsaMainActivityRepositoryImpl @Inject constructor(
    private val backendCommunicationService: BackendCommunicationService
): OsaMainActivityRepository {
    override fun getAllExams(context: Context): List<ExamsDomainDto> {
        return backendCommunicationService
            .getAllExams(context)
            .mapNotNull { toExamDomainDto(it) }
    }

    override fun getAllThemesByExamId(context: Context, examId: Int): ThemesDomainDto? {
        return backendCommunicationService
            .getAllThemesByExamId(context, examId)
            .let { toThemesDomainDto(it) }
    }

    override fun getQuestionsByThemeId(
        context: Context,
        themeId: Int
    ): QuestionsForTestingDomainDto? {
        return backendCommunicationService
            .getQuestionsByThemeId(context, themeId)
            .let { toQuestionsForTestingDomainDto(it) }
    }
}
