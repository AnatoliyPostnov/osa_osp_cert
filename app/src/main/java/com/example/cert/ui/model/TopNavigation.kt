package com.example.cert.ui.model

import android.content.Context
import androidx.navigation.NavController
import com.example.cert.ui.viewmodel.QuestionActivityViewModel


data class TopNavigation(
    val navController: NavController,
    val viewModel: QuestionActivityViewModel,
    val activityContext: Context
)
