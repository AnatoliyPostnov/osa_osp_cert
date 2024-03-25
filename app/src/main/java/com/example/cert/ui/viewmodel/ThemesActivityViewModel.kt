package com.example.cert.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cert.domain.usecase.OsaMainActivityUseCases
import com.example.cert.ui.model.ThemesActivityState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemesActivityViewModel (
    private val osaMainActivityUseCases: OsaMainActivityUseCases,
    private val applicationContext: Context
): ViewModel() {
    private val _mainActivityViewModelState = MutableStateFlow(ThemesActivityState())
    val uiState: StateFlow<ThemesActivityState> = _mainActivityViewModelState.asStateFlow()

    fun getThemesByExamId(examId: Int?) {
        val themes = osaMainActivityUseCases.getAllThemesByExamId(context = applicationContext, examId = examId)
        _mainActivityViewModelState.value.themes = themes.themes
        _mainActivityViewModelState.value.examId = themes.examId
    }

    class Factory (
        private val context: Context,
        private val osaMainActivityUseCases: OsaMainActivityUseCases,
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == ThemesActivityViewModel::class.java)
            return ThemesActivityViewModel(osaMainActivityUseCases, context) as T
        }
    }
}
