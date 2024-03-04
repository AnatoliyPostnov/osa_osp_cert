package com.example.cert.ui.component

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.cert.ui.model.MainMenuDto
import com.example.cert.ui.theme.Brown

@Composable
fun MainWindowMenu(mainMenuDto: MainMenuDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Box() {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = mainMenuDto.pictureId),
                    contentDescription = "osa",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(100.dp)
                )
                OutlinedButton(
                    {
                        startActivity(mainMenuDto.context, mainMenuDto.intent, mainMenuDto.options)
                    },
                    colors = ButtonDefaults.buttonColors(Brown),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 10.dp)
                ) {
                    Text(
                        mainMenuDto.examsDomainDto.content,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
