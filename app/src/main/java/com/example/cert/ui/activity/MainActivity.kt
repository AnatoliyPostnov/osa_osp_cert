package com.example.cert.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.cert.ui.component.MainWindow
import com.example.cert.ui.utils.getAppComponent
import com.example.cert.ui.viewmodel.MainActivityViewModel
import javax.inject.Inject


class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels {
        getAppComponent().factory.create(applicationContext)
    }

//    @Inject
//    lateinit var factory: MainActivityViewModel.Factory.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
//        getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            MainWindow(mainActivityViewModel = viewModel, context = this, savedInstanceState = savedInstanceState)
        }
    }
}
