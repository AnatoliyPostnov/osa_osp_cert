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
import com.example.cert.domain.repository.QuestionActivityRepository
import javax.inject.Inject

class QuestionActivityRepositoryImpl @Inject constructor(
    private val backendCommunicationService: BackendCommunicationService
): QuestionActivityRepository {
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

    override fun getQuestionsByThemeIdAndExamId(
        context: Context,
        themeId: Int,
        examId: Int
    ): QuestionsForTestingDomainDto {
        return backendCommunicationService
            .getQuestionsByThemeId(context, themeId)
            .let { toQuestionsForTestingDomainDto(it) }    }

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
        val correctAnswersPercentage = (((1.0 * rightAnswers)/(rightAnswers + wrongAnswers)) * 100).toInt()
        return ExamTestResultDomainDto(
            rightAnswers = rightAnswers,
            wrongAnswers = wrongAnswers,
            correctAnswersPercentage = correctAnswersPercentage.toString(),
            questionsResult = questionResults
        )
    }
}
