package com.example.cert.data.repository.itrf

import android.content.Context
import com.example.cert.data.repository.dto.ExamsDto
import com.example.cert.data.repository.dto.ThemesDto

interface BackendCommunicationService {
    fun getAllExams(context: Context): List<ExamsDto>
    fun getAllThemesByExamId(context: Context, examId: Int): ThemesDto
}