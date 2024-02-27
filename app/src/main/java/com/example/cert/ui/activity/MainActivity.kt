package com.example.cert.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cert.configuration.MainApp
import com.example.cert.ui.component.MainWindow


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as MainApp).appComponent.inject(this)
        setContent {
            MainWindow(context = this, savedInstanceState = savedInstanceState)
        }
    }
}
