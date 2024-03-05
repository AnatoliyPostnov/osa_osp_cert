package com.example.cert.ui.viewmodel

import android.content.Context
import com.example.cert.domain.usecase.OsaMainActivityUseCases
import javax.inject.Inject

class Factory @Inject constructor(
    private val osaMainActivityUseCases: OsaMainActivityUseCases
) {
    fun createMainActivity(context: Context): MainActivityViewModel.Factory {
        return MainActivityViewModel.Factory(context, osaMainActivityUseCases)
    }

    fun createThemesActivityViewModel(context: Context): ThemesActivityViewModel.Factory {
        return ThemesActivityViewModel.Factory(context, osaMainActivityUseCases)
    }

    fun createTestActivityViewModel(context: Context): TestActivityViewModel.Factory {
        return TestActivityViewModel.Factory(context, osaMainActivityUseCases)
    }
}