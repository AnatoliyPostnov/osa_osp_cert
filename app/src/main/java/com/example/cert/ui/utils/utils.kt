package com.example.cert.ui.utils

import androidx.activity.ComponentActivity
import com.example.cert.configuration.AppComponent
import com.example.cert.configuration.MainApp

fun ComponentActivity.getAppComponent(): AppComponent =
    (applicationContext as MainApp).appComponent
