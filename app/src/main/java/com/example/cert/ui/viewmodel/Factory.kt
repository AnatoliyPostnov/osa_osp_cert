package com.example.cert.ui.viewmodel

import android.content.Context
import com.example.cert.domain.usecase.QuestionActivityUseCases
import javax.inject.Inject

class Factory @Inject constructor(
    private val questionActivityUseCases: QuestionActivityUseCases
) {
    fun createMainActivity(context: Context): MainActivityViewModel.Factory {
        return MainActivityViewModel.Factory(context, questionActivityUseCases)
    }

    fun createThemesActivityViewModel(context: Context): ThemesActivityViewModel.Factory {
        return ThemesActivityViewModel.Factory(context, questionActivityUseCases)
    }

    fun createTestActivityViewModel(context: Context): QuestionActivityViewModel.Factory {
        return QuestionActivityViewModel.Factory(context, questionActivityUseCases)
    }

    fun createQuestionsResultViewModel(context: Context): QuestionsResultViewModel.Factory {
        return QuestionsResultViewModel.Factory(context, questionActivityUseCases)
    }
}
