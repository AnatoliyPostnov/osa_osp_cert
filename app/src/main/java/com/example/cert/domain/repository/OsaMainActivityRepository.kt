package com.example.cert.domain.repository

import com.example.cert.domain.model.ExamsDomainDto

interface OsaMainActivityRepository {
    fun getAllExams(): List<ExamsDomainDto>
}