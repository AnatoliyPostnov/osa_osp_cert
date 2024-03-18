package com.example.cert.ui.activity.question

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.cert.R
import com.example.cert.domain.model.AnswerDomainDto
import com.example.cert.ui.activity.MainActivity
import com.example.cert.ui.model.ResultItem
import com.example.cert.ui.theme.Brown
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
            Column {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .weight(1.1f)) {
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
                                        Question(it)
                                        it.answers.forEach { answer -> Answers(answer) }
                                        Text(
                                            text = "Your answer: ${it.yourAnswer}",
                                            style = MaterialTheme.typography.titleMedium,
                                            textAlign = TextAlign.Start
                                        )
                                        Text(
                                            text = "Correct answer: ${it.rightAnswer}",
                                            style = MaterialTheme.typography.titleMedium,
                                            textAlign = TextAlign.Start
                                        )
                                        Text(
                                            text = "Explanation: ${it.explanation}",
                                            style = MaterialTheme.typography.titleMedium,
                                            textAlign = TextAlign.Start
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.1f),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            val intent = Intent(activityContext, MainActivity::class.java)
                            ContextCompat.startActivity(
                                activityContext,
                                intent,
                                null
                            )
                        },
                        colors = ButtonDefaults.buttonColors(Brown),
                    ) {
                        Text("Return to main menu")
                    }
                }
            }
        }
    }

    @Composable
    fun Question(item: ResultItem) {
        LazyRow {
            item {
                MarkdownText(markdown = item.question.trimIndent(), style = MaterialTheme.typography.titleMedium)
            }
        }
    }

    @Composable
    fun Answers(answer: AnswerDomainDto) {
        Text(
            text = "${answer.answerId}. ${answer.content}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
            textAlign = TextAlign.Start
        )
    }
}
