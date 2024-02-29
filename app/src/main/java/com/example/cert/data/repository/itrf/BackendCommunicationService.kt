package com.example.cert.data.repository.itrf

import android.content.Context
import com.example.cert.data.repository.dto.ExamsDto

interface BackendCommunicationService {
    fun getAllExams(context: Context): List<ExamsDto>
}