package com.example.cert.domain.repository

import android.content.Context
import com.example.cert.domain.model.ExamsDomainDto

interface OsaMainActivityRepository {
    fun getAllExams(context: Context): List<ExamsDomainDto>
}