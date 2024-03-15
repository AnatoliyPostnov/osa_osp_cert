package com.example.cert.ui.model


data class ResultItem(
    val id: Int,
    val isRight: Boolean,
    val yourAnswer: String,
    val rightAnswer: String,
    val explanation: String,
    val content: String
)
