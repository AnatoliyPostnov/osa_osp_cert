package com.example.cert.ui.viewmodel

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cert.domain.model.ExamTestResultRequestDto
import com.example.cert.domain.model.QuestionsForTestingDomainDto
import com.example.cert.domain.usecase.OsaMainActivityUseCases
import com.example.cert.ui.model.TestActivityState
import com.example.cert.ui.model.TopItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuestionActivityViewModel (
    private val osaMainActivityUseCases: OsaMainActivityUseCases,
    private val applicationContext: Context
): ViewModel() {
    private val _testActivityViewModelState = MutableStateFlow(TestActivityState())
    val uiState: StateFlow<TestActivityState> = _testActivityViewModelState.asStateFlow()

    class Factory (
        private val context: Context,
        private val osaMainActivityUseCases: OsaMainActivityUseCases,
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == QuestionActivityViewModel::class.java)
            return QuestionActivityViewModel(osaMainActivityUseCases, context) as T
        }
    }

    fun findQuestionsByThemeId(context: Context, themeId: Int?) {
        val questions = _testActivityViewModelState.value.questions
            ?: osaMainActivityUseCases.getQuestionsByThemeId(context, themeId)
        val answerSelected = _testActivityViewModelState.value.answerSelected
        if (answerSelected.isEmpty()) {
            questions.questions.forEach { question -> question.answers.forEach { answerSelected["${question.questionId} ${it.answerId}"] = false } }
        }
        val topItems = _testActivityViewModelState.value.topItems
        if(topItems.isEmpty()) {
            questions.questions.forEach { topItems[it.questionId] = TopItem(route = it.questionId, question = it) }
        }
        updateState(questions)
    }

    fun setChooseAnswerButton(questionId: Int, answerId: Int) {
        val currentQuestion = getCurrentQuestion(questionId)
        val questionType = currentQuestion?.type

        val answerIcons = _testActivityViewModelState.value.answerSelected

        if (questionType == "one_answer") {
            currentQuestion.answers.forEach {
                val isChose = it.answerId == answerId
                it.userAnswer = isChose
                answerIcons["$questionId ${it.answerId}"] = isChose
            }
        } else {
            val answer = currentQuestion?.answers?.find { it.answerId == answerId }
            val isChose = answer?.userAnswer != true
            answer?.userAnswer = isChose
            answerIcons["$questionId ${answer?.answerId}"] = isChose
        }
        updateState(_testActivityViewModelState.value.questions)
    }

    fun commitQuestion(questionId: Int?) {
        if (questionId == null) return
        val commitQuestion = getCurrentQuestion(questionId) ?: throw RuntimeException("commit question can`t be null")
        val questions = _testActivityViewModelState.value.questions ?: throw RuntimeException("questions can`t be null")

        val result = _testActivityViewModelState.value.examTestResultRequestDto
            ?: ExamTestResultRequestDto(questions.themeId, emptyList())
        val currentCommittedQuestions = result.questions.toMutableSet()
        currentCommittedQuestions.add(commitQuestion)

        val currentTopItem = _testActivityViewModelState.value.topItems[questionId]
            ?: throw RuntimeException("questionId can`t be null")
        _testActivityViewModelState.value.topItems[questionId] = currentTopItem.copy(isCommitted = true, color = Color.Blue)

        updateState(examTestResultRequestDto = result.copy(questions = currentCommittedQuestions.toList()))
    }

    private fun updateState(questions: QuestionsForTestingDomainDto?) {
        _testActivityViewModelState.update { currentState ->
            currentState.copy(
                questions = questions
            )
        }
    }

    private fun updateState(examTestResultRequestDto: ExamTestResultRequestDto) {
        _testActivityViewModelState.update { currentState ->
            currentState.copy(
                examTestResultRequestDto = examTestResultRequestDto,
            )
        }
    }

    private fun getCurrentQuestion(questionId: Int) =
        _testActivityViewModelState.value.questions?.questions?.find { it.questionId == questionId }

}
