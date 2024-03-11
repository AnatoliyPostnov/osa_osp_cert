package com.example.cert.ui.viewmodel

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.sharp.Add
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cert.domain.model.QuestionsForTestingDomainDto
import com.example.cert.domain.usecase.OsaMainActivityUseCases
import com.example.cert.ui.model.TestActivityState
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
        val questions = osaMainActivityUseCases.getQuestionsByThemeId(context, themeId)
        val answerIcons = mutableStateMapOf<String, ImageVector>()
        questions.questions.forEach { question -> question.answers.forEach { answerIcons["${question.questionId} ${it.answerId}"] = Icons.Sharp.Add } }
        updateState(questions, answerIcons)
    }

    fun setChooseAnswerButton(questionId: Int, answerId: Int) {
        val currentQuestions = _testActivityViewModelState.value.questions?.questions?.find { it.questionId == questionId }
        val questionType = currentQuestions?.type

        val answerIcons = _testActivityViewModelState.value.answerIcons

        if (questionType == "one_answer") {
            currentQuestions.answers.forEach {
                val isChose = it.answerId == answerId
                it.userAnswer = isChose
                answerIcons["$questionId ${it.answerId}"] = if (isChose) { Icons.Filled.Check } else { Icons.Filled.Add }
            }
        } else {
            val answer = currentQuestions?.answers?.find { it.answerId == answerId }
            val isChose = answer?.userAnswer != true
            answer?.userAnswer = isChose
            answerIcons["$questionId ${answer?.answerId}"] = if (isChose) { Icons.Filled.Check } else { Icons.Filled.Add }
        }

        updateState(_testActivityViewModelState.value.questions, answerIcons)
    }

    private fun updateState(questions: QuestionsForTestingDomainDto?, answerIcons: MutableMap<String, ImageVector>) {
        _testActivityViewModelState.update { currentState ->
            currentState.copy(questions = questions, answerIcons = answerIcons)
        }
    }

}
