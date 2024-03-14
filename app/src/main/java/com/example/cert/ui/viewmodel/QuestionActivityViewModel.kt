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
        if (topItems.isEmpty()) {
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
    }

    fun commitQuestion(questionId: Int?): Boolean {
        if (questionId == null) return false
        val commitQuestion = getCurrentQuestion(questionId)
        if (commitQuestion?.answers?.find { it.userAnswer == true } == null) return false
        val questions = _testActivityViewModelState.value.questions ?: return false

        val examTestResult = _testActivityViewModelState.value.examTestResultRequestDto
            ?: ExamTestResultRequestDto(questions.themeId, emptyList())
        val currentCommittedQuestions = examTestResult.questions.toMutableSet()
        currentCommittedQuestions.add(commitQuestion)

        val currentTopItem = _testActivityViewModelState.value.topItems[questionId]
            ?: throw RuntimeException("questionId can`t be null")
        _testActivityViewModelState.value.topItems[questionId] = currentTopItem.copy(isCommitted = true, color = Color.Blue)

        updateState(examTestResultRequestDto = examTestResult.copy(questions = currentCommittedQuestions.toList()))
        return true
    }

    fun uncommittedQuestion(questionId: Int?) {
        if (questionId == null) return
        val commitQuestion = getCurrentQuestion(questionId)

        val examTestResultOld = _testActivityViewModelState.value.examTestResultRequestDto
            ?: throw RuntimeException("examTestResultRequest can`t be null")

        val examTestResult = examTestResultOld.copy(questions = examTestResultOld.questions.filter { it == commitQuestion })

        val currentTopItem = _testActivityViewModelState.value.topItems[questionId]
            ?: throw RuntimeException("questionId can`t be null")
        _testActivityViewModelState.value.topItems[questionId] = currentTopItem.copy(isCommitted = false, color = Color.LightGray)

        updateState(examTestResultRequestDto = examTestResult)
    }

    fun getCommitButtonState(questionId: Int?): Boolean {
        if (questionId == null) return false
        return _testActivityViewModelState.value.topItems[questionId]?.isCommitted == true
    }

    fun getSendResultButtonState(): Boolean {
        return _testActivityViewModelState.value.topItems.values.find { !it.isCommitted } == null
    }

    fun getNextRoute(currentRoute: Int?): Int? {
        val questionsSize = _testActivityViewModelState.value.questions?.questions?.size ?: 0
        var nextRoute = currentRoute
        if (currentRoute != null) {
            for (i in nextRoute!!..< questionsSize) {
                if (_testActivityViewModelState.value.topItems[i + 1]?.isCommitted != true) {
                    nextRoute = i + 1
                    break
                }
            }
            if (nextRoute == currentRoute) {
                for (i in 0..< questionsSize) {
                    if (_testActivityViewModelState.value.topItems[i + 1]?.isCommitted != true) {
                        nextRoute = i + 1
                        break
                    }
                }
            }
        }
        return nextRoute
    }

    fun getPrevRoute(currentRoute: Int?): Int? {
        val questionsSize = _testActivityViewModelState.value.questions?.questions?.size ?: 0
        var prevRoute = currentRoute
        if (currentRoute != null) {
            for (i in currentRoute downTo 2 ) {
                if (_testActivityViewModelState.value.topItems[i - 1]?.isCommitted != true) {
                    prevRoute = i - 1
                    break
                }
            }
            if (prevRoute == currentRoute) {
                for (i in questionsSize - 1 downTo 0) {
                    if (_testActivityViewModelState.value.topItems[i + 1]?.isCommitted != true) {
                        prevRoute = i + 1
                        break
                    }
                }
            }
        }
        return prevRoute
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
