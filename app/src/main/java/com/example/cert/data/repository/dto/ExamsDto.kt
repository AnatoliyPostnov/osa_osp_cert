package com.example.cert.data.repository.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ExamsDto(
    @JsonProperty("exam_id")
    val examId: Int,
    val content: String,
    @JsonProperty("picture_file_name")
    val pictureFileName: String,
)
