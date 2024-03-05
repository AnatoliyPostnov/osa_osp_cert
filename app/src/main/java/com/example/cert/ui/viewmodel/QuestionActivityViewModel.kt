package com.example.cert.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cert.domain.usecase.OsaMainActivityUseCases
import com.example.cert.ui.model.TestActivityState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    fun getQuestionsByThemeId(context: Context, themeId: Int?) {
        val questions = osaMainActivityUseCases.getQuestionsByThemeId(context, themeId)
        _testActivityViewModelState.value.questions = questions
    }

}
