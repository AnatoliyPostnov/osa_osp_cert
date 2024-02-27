package com.example.cert.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cert.ui.model.MainActivityState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivityViewModel : ViewModel() {
    private val _mainActivityViewModelState = MutableStateFlow(MainActivityState())
    val uiState: StateFlow<MainActivityState> = _mainActivityViewModelState.asStateFlow()
}
