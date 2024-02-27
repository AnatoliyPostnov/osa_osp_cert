package com.example.cert.ui.component

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cert.ui.viewmodel.MainActivityViewModel
import com.example.cert.R
import com.example.cert.ui.model.MainMenuDto

@Composable
fun MainWindow(
    mainActivityViewModel: MainActivityViewModel = viewModel(),
    context: Context,
    savedInstanceState: Bundle?
) {
    val uiState by mainActivityViewModel.uiState.collectAsState()

    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = "background",
        modifier = Modifier.fillMaxSize().padding(top = 70.dp),
        contentScale = ContentScale.FillBounds,
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "OCA/OCP JAVA SE 8 PROGRAMMER",
            fontSize = 20.sp,
            style = MaterialTheme.typography.bodyLarge,                )
        Text(
            "PRACTICE TESTS",
            color = Color.Red,
            fontSize = 20.sp,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            """
                            This application contains a set of tests from the book "OCA: Oracle Certified Associate Java SE 8 Programmer I Study Guide" and "OCP Oracle Certified Professional Java SE 11 Programmer II Study Guide" to prepare for the certified exam from Oracle in JAVA SE.

                        Select a section below:
                    """.trimIndent(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(10.dp),
            textAlign = TextAlign.Justify
        )
        MainWindowMenu(MainMenuDto.createOsaActivity(context, savedInstanceState))
        MainWindowMenu(MainMenuDto.createOspActivity(context, savedInstanceState))
    }
}
