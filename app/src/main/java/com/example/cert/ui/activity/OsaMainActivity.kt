package com.example.cert.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.cert.R
import com.example.cert.ui.theme.Brown
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
            val activityContext = this
            viewModel.getThemesByExamId(activityContext.intent.extras?.getInt("exam_id"))
            val themesState = uiState.themes
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)) {
                    Column {
                        Text("Oracle Certified Associate", style = typography.titleLarge, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        Text("exam topics", style = typography.titleMedium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(5f)) {
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()) {
                        themesState.forEach {
                            item {
                                OutlinedButton(
                                    {
                                        val intent = Intent(activityContext, QuestionsActivity::class.java)
                                        intent.putExtra("theme_id", it.id)
                                        ContextCompat.startActivity(
                                            activityContext,
                                            intent,
                                            null
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(Brown),
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                        .fillMaxWidth()
                                        .height(50.dp)
                                ) {
                                    Text(
                                        it.content, style = typography.titleMedium, textAlign = TextAlign.Justify, modifier = Modifier.padding(3.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.backbutton),
                            contentDescription = "backbutton",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(35.dp)
                                .padding(2.dp)
                                .clickable {
                                    val intent = Intent(activityContext, MainActivity::class.java)
                                    ContextCompat.startActivity(
                                        activityContext,
                                        intent,
                                        null
                                    )
                                }
                        )
                        Text("back", style = typography.titleMedium, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}
