package com.example.cert.ui.activity.question

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.cert.R
import com.example.cert.ui.utils.getAppComponent
import com.example.cert.ui.viewmodel.Factory
import com.example.cert.ui.viewmodel.QuestionsResultViewModel
import dev.jeziellago.compose.markdowntext.MarkdownText
import javax.inject.Inject

class QuestionsResultActivity : ComponentActivity() {

    @Inject
    lateinit var factory: Factory

    private val viewModel: QuestionsResultViewModel by viewModels {
        factory.createQuestionsResultViewModel(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            val activityContext = this
            val state by viewModel.uiState.collectAsState()

            viewModel.setTestResult(activityContext.intent.extras?.getString("test_result"))

            LazyColumn {
                state.resultItems.values.forEach {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            shape = RoundedCornerShape(15.dp),
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (it.isRight) {
                                    colorResource(R.color.light_green)
                                } else {
                                    colorResource(R.color.light_red)
                                }
                            )
                        ) {
                            Column(modifier = Modifier.padding(15.dp)) {
                                MarkdownText(
                                    markdown = it.content.trimIndent(),
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(text = "Your answer: ${it.yourAnswer}")
                                Text(text = "Correct answer: ${it.rightAnswer}")
                                Text(text = "Explanation: ${it.explanation}")
                            }
                        }
                    }
                }
            }
        }
    }
}
