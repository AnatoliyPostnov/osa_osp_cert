package com.example.cert.data.repository.mapper

import com.example.cert.data.repository.dto.ExamsDto
import com.example.cert.data.repository.dto.QuestionsForTestingDto
import com.example.cert.data.repository.dto.ThemesDto
import com.example.cert.domain.model.AnswerDomainDto
import com.example.cert.domain.model.ExamsDomainDto
import com.example.cert.domain.model.QuestionDomainDto
import com.example.cert.domain.model.QuestionsForTestingDomainDto
import com.example.cert.domain.model.ThemeDomainDto
import com.example.cert.domain.model.ThemesDomainDto

fun toExamDomainDto(examsDto: ExamsDto?): ExamsDomainDto? {
    return examsDto?.let {
        ExamsDomainDto(
            examId = it.examId,
            content = it.content,
            pictureFileName = it.pictureFileName
        )
    }
}

fun toThemesDomainDto(themesDto: ThemesDto?): ThemesDomainDto? {
    return themesDto?.let {
        ThemesDomainDto(
            examId = themesDto.examId,
            themes = themesDto
                .themes
                .map {
                    ThemeDomainDto(
                        id = it.id,
                        content = it.content
                    )
                }
        )
    }
}

fun toQuestionsForTestingDomainDto(questionsForTestingDto: QuestionsForTestingDto): QuestionsForTestingDomainDto {
    return questionsForTestingDto.let {
        QuestionsForTestingDomainDto(
            themeId = it.themeId,
            examId = it.examId,
            themeContent = it.themeContent,
            questions = it.questions.map { question ->
                QuestionDomainDto(
                    content = question.content,
                    questionId = question.questionId,
                    type = question.type,
                    answers = question.answers.map { answer ->
                        AnswerDomainDto(
                            answerId = answer.answerId,
                            content = answer.content
                        )
                    }
                )
            }
        )
    }
}
