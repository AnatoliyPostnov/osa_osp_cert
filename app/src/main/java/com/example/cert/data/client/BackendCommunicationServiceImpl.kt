package com.example.cert.data.client

import android.content.Context
import com.example.cert.data.repository.dto.ExamsDto
import com.example.cert.data.repository.dto.ThemesDto
import com.example.cert.data.repository.itrf.BackendCommunicationService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import javax.inject.Inject

class BackendCommunicationServiceImpl @Inject constructor(
    private val objectMapper: ObjectMapper
): BackendCommunicationService {
    override fun getAllExams(context: Context): List<ExamsDto> {
        return context.assets.open("getAllExams.json").use {
            val json = objectMapper.readTree(it)
            objectMapper.treeToValue<List<ExamsDto>>(json)
        }
    }

    override fun getAllThemesByExamId(context: Context, examId: Int): ThemesDto {
        return context.assets.open("getOsaThemes.json").use {
            val json = objectMapper.readTree(it)
            objectMapper.treeToValue<ThemesDto>(json)
        }
    }
}