package com.example.cert.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cert.domain.usecase.OsaMainActivityUseCases
import com.example.cert.ui.model.MainActivityState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivityViewModel (
    private val osaMainActivityUseCases: OsaMainActivityUseCases,
    private val applicationContext: Context
): ViewModel() {
    private val _mainActivityViewModelState = MutableStateFlow(MainActivityState())
    val uiState: StateFlow<MainActivityState> = _mainActivityViewModelState.asStateFlow()

    init {
        setExams(applicationContext)
    }

    private fun setExams(context: Context) {
        val exams = osaMainActivityUseCases.getAllExams(context)
        _mainActivityViewModelState.value.examsDomainDto = exams
    }

    class Factory (
        private val context: Context,
        private val osaMainActivityUseCases: OsaMainActivityUseCases,
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == MainActivityViewModel::class.java)
            return MainActivityViewModel(osaMainActivityUseCases, context) as T
        }
    }
}
