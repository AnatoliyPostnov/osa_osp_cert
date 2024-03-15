package com.example.cert.data.repository

import android.content.Context
import com.example.cert.data.repository.itrf.BackendCommunicationService
import com.example.cert.data.repository.mapper.toExamDomainDto
import com.example.cert.data.repository.mapper.toQuestionsForTestingDomainDto
import com.example.cert.data.repository.mapper.toThemesDomainDto
import com.example.cert.domain.model.ExamTestResultDomainDto
import com.example.cert.domain.model.ExamTestResultRequestDomainDto
import com.example.cert.domain.model.ExamsDomainDto
import com.example.cert.domain.model.QuestionResult
import com.example.cert.domain.model.QuestionsForTestingDomainDto
import com.example.cert.domain.model.ThemesDomainDto
import com.example.cert.domain.repository.OsaMainActivityRepository
import javax.inject.Inject

class OsaMainActivityRepositoryImpl @Inject constructor(
    private val backendCommunicationService: BackendCommunicationService
): OsaMainActivityRepository {
    override fun getAllExams(context: Context): List<ExamsDomainDto> {
        return backendCommunicationService
            .getAllExams(context)
            .mapNotNull { toExamDomainDto(it) }
    }

    override fun getAllThemesByExamId(context: Context, examId: Int): ThemesDomainDto? {
        return backendCommunicationService
            .getAllThemesByExamId(context, examId)
            .let { toThemesDomainDto(it) }
    }

    override fun getQuestionsByThemeId(
        context: Context,
        themeId: Int
    ): QuestionsForTestingDomainDto? {
        return backendCommunicationService
            .getQuestionsByThemeId(context, themeId)
            .let { toQuestionsForTestingDomainDto(it) }
    }

    override fun getOsaTestResult(
        context: Context,
        request: ExamTestResultRequestDomainDto
    ): ExamTestResultDomainDto {
        val resultAnswersDto = backendCommunicationService.getAnswersByThemeId(context, request.themeId)
        val questionResults = resultAnswersDto.questions.map {
            val userAnswer = request.questions.find { q -> q.questionId == it.questionId }?.answers
                ?.filter { answer -> answer.userAnswer == true }
                ?.map { answer -> answer.answerId }
                ?: throw RuntimeException("User answer can`t be null")
            val correctAnswers = it.correctAnswers
            val isRight = userAnswer.containsAll(correctAnswers) && correctAnswers.containsAll(userAnswer)
            QuestionResult(
                id = it.questionId,
                isRight = isRight,
                yourAnswer = userAnswer.joinToString(", "),
                rightAnswer = correctAnswers.joinToString(", "),
                explanation = it.explanation
            )
        }
        val rightAnswers = questionResults.count { it.isRight }
        val wrongAnswers = questionResults.count { !it.isRight }
        val correctAnswersPercentage = (rightAnswers/(rightAnswers + wrongAnswers)) * 100
        return ExamTestResultDomainDto(
            rightAnswers = rightAnswers,
            wrongAnswers = wrongAnswers,
            correctAnswersPercentage = correctAnswersPercentage.toString(),
            questionsResult = questionResults
        )
    }
}
