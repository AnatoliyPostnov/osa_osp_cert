package com.example.cert.data.repository.mapper

import com.example.cert.data.repository.dto.ExamsDto
import com.example.cert.domain.model.ExamsDomainDto

fun toExamDomainDto(examsDto: ExamsDto?): ExamsDomainDto? {
    return examsDto?.let {
        ExamsDomainDto(
            examId = it.examId,
            content = it.content,
            pictureFileName = it.pictureFileName
        )
    }
}
