package com.example.cert.domain.repository

import android.content.Context
import com.example.cert.domain.model.ExamsDomainDto
import com.example.cert.domain.model.ThemesDomainDto

interface OsaMainActivityRepository {
    fun getAllExams(context: Context): List<ExamsDomainDto>
    fun getAllThemesByExamId(context: Context, examId: Int): ThemesDomainDto?
}