package com.example.cert.data.repository

import com.example.cert.data.repository.itrf.BackendCommunicationService
import com.example.cert.data.repository.mapper.toExamDomainDto
import com.example.cert.domain.model.ExamsDomainDto
import com.example.cert.domain.repository.OsaMainActivityRepository

class OsaMainActivityRepositoryImpl(
    private val backendCommunicationService: BackendCommunicationService
): OsaMainActivityRepository {
    override fun getAllExams(): List<ExamsDomainDto> {
        return backendCommunicationService
            .getAllExams()
            .mapNotNull { toExamDomainDto(it) }
    }
}
