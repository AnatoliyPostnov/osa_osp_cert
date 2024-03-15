package com.example.cert.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cert.configuration.JacksonConfig.Companion.objectMapper
import com.example.cert.domain.usecase.OsaMainActivityUseCases
import com.example.cert.ui.model.ResultItem
import com.example.cert.ui.model.TestActivityState
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuestionsResultViewModel(
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
            require(modelClass == QuestionsResultViewModel::class.java)
            return QuestionsResultViewModel(osaMainActivityUseCases, context) as T
        }
    }
    fun setTestResult(testResult: String?) {
        val resultMutableMap = mutableStateMapOf<Int, ResultItem>()

        (objectMapper.readTree(testResult) as? ArrayNode)
            ?.map {
                val resultItem = objectMapper.treeToValue(it as? ObjectNode, ResultItem::class.java)
                resultMutableMap[resultItem.id] = resultItem
            }
        _testActivityViewModelState.update { currentState ->
            currentState.copy(resultItems = resultMutableMap)
        }
    }
}
