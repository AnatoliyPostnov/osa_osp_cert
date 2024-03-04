package com.example.cert.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.cert.ui.utils.getAppComponent
import com.example.cert.ui.viewmodel.Factory
import com.example.cert.ui.viewmodel.ThemesActivityViewModel
import javax.inject.Inject

class OsaMainActivity : ComponentActivity() {

    @Inject
    lateinit var factory: Factory

    private val viewModel: ThemesActivityViewModel by viewModels {
        factory.createThemesActivityViewModel(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.collectAsState()
            viewModel.getThemesByExamId(this.intent.extras?.getInt("exam_id"))
            val themesState = uiState.themes
            Column {
                themesState.forEach {
                    Text(it.content)
                }
            }
        }
    }
}
