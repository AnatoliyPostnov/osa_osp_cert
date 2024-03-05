package com.example.cert.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.cert.ui.utils.getAppComponent
import com.example.cert.ui.viewmodel.Factory
import com.example.cert.ui.viewmodel.TestActivityViewModel
import javax.inject.Inject

class TestActivity : ComponentActivity() {

    @Inject
    lateinit var factory: Factory

    private val viewModel: TestActivityViewModel by viewModels {
        factory.createTestActivityViewModel(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val activityContext = this



            Column(
                modifier = Modifier.fillMaxSize()
            ) {

            }
        }
    }
}
