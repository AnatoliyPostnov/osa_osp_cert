package com.example.cert.data.client

import android.content.Context
import com.example.cert.data.repository.dto.ExamsDto
import com.example.cert.data.repository.dto.QuestionsForTestingDto
import com.example.cert.data.repository.dto.ResultAnswersDto
import com.example.cert.data.repository.dto.ThemesDto
import com.example.cert.data.repository.itrf.BackendCommunicationService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
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

    override fun getQuestionsByThemeId(context: Context, themeId: Int): QuestionsForTestingDto {
        return context.assets.open("getQuestionsByThemeId.json").use {
            val json = (objectMapper.readTree(it) as? ArrayNode)
                ?.find { item ->  (item as? ObjectNode)?.get("theme_id")?.asInt() == themeId }
                ?: throw RuntimeException("Theme id $themeId wasn't found in data")
            objectMapper.treeToValue<QuestionsForTestingDto>(json)
        }
    }

    override fun getAnswersByThemeId(context: Context, themeId: Int): ResultAnswersDto {
        return context.assets.open("getAnswersByThemeId.json").use {
            val json = (objectMapper.readTree(it) as? ArrayNode)
                ?.find { item ->  (item as? ObjectNode)?.get("theme_id")?.asInt() == themeId }
                ?: throw RuntimeException("Theme id $themeId wasn't found in data")
            objectMapper.treeToValue<ResultAnswersDto>(json)
        }
    }
}
