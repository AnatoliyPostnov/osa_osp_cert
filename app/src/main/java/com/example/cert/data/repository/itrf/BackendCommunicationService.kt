package com.example.cert.data.repository.itrf

import com.example.cert.data.repository.dto.ExamsDto

interface BackendCommunicationService {
    fun getAllExams(): List<ExamsDto>
}