package com.example.cert.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.cert.ui.component.MainWindow
import com.example.cert.ui.utils.getAppComponent
import com.example.cert.ui.viewmodel.Factory
import com.example.cert.ui.viewmodel.MainActivityViewModel
import javax.inject.Inject


class MainActivity : ComponentActivity() {

    @Inject
    lateinit var factory: Factory

    private val viewModel: MainActivityViewModel by viewModels {
        factory.createMainActivity(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            MainWindow(mainActivityViewModel = viewModel, context = this, savedInstanceState = savedInstanceState)
        }
    }
}
