package com.example.cert.ui.activity.question

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cert.configuration.JacksonConfig.Companion.objectMapper
import com.example.cert.domain.model.AnswerDomainDto
import com.example.cert.domain.model.QuestionDomainDto
import com.example.cert.ui.model.TestActivityState
import com.example.cert.ui.model.TopItem
import com.example.cert.ui.model.TopNavigation
import com.example.cert.ui.theme.Brown
import com.example.cert.ui.utils.getAppComponent
import com.example.cert.ui.viewmodel.Factory
import com.example.cert.ui.viewmodel.QuestionActivityViewModel
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.lang.RuntimeException
import javax.inject.Inject

class QuestionsActivity : ComponentActivity() {

    @Inject
    lateinit var factory: Factory

    private val viewModel: QuestionActivityViewModel by viewModels {
        factory.createTestActivityViewModel(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            val activityContext = this
            val themeId = activityContext.intent.extras?.getInt("theme_id")

            viewModel.findQuestionsByThemeId(activityContext, themeId)

            val state by viewModel.uiState.collectAsState()

            val navController = rememberNavController()

            if (state.questions == null) {
                Text(text = "questions were not found")
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)) {
                        Header(state)
                    }
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(5f)) {
                        QuestionMainScreen(navController, viewModel, activityContext, state)
                    }
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)) {
                        BottomMenu(navController, viewModel, state)
                    }
                }
            }
        }
    }
}

@Composable
fun Header(state: TestActivityState) {
    Text(
        state.questions?.themeContent ?: "Content was not found",
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun QuestionMainScreen(navController: NavController, viewModel: QuestionActivityViewModel, activityContext: QuestionsActivity, state: TestActivityState) {
    val navigation = TopNavigation(navController = navController, viewModel = viewModel, activityContext = activityContext)
    Scaffold(
        modifier = Modifier
            .background(Color.Yellow)
            .fillMaxWidth(),
        topBar = {
            TopNavigation(navigation, state)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            NavigationGraph(navigation)
        }
    }
}

@Composable
fun BottomMenu(navController: NavController, viewModel: QuestionActivityViewModel, state: TestActivityState) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.toInt()
    val nextRote = viewModel.getNextRoute(currentRoute)
    val prevRote = viewModel.getPrevRoute(currentRoute)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = {
                navController.navigate(prevRote.toString()) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = ButtonDefaults.buttonColors(Brown)
        ) {
            Text("prev")
        }
        Spacer(modifier = Modifier.weight(0.5f))
        if (!viewModel.getCommitButtonState(currentRoute)) {
            Button(
                onClick = {
                    if (viewModel.commitQuestion(currentRoute)) {
                        navController.navigate(nextRote.toString()) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(Brown)
            ) {
                Text("commit answer")
            }
        } else {
            Button(
                onClick = { viewModel.uncommittedQuestion(currentRoute) },
                colors = ButtonDefaults.buttonColors(Brown)
            ) {
                Text("uncommit answer")
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Button(
            onClick = {
                navController.navigate(nextRote.toString()) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = ButtonDefaults.buttonColors(Brown)
        ) {
            Text("next")
        }
    }
}

@Composable
fun TopNavigation(navigationState: TopNavigation, state: TestActivityState) {
    val navBackStackEntry by navigationState.navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .padding(5.dp),
    ) {
        item {
            state.topItems.forEach { (route, screen) ->
                if (currentRoute == route.toString()) {
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .padding(5.dp)
                            .background(screen.color)
                            .clickable {
                                navigationState.navController.navigate(screen.route.toString()) {
                                    popUpTo(navigationState.navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(27.dp)
                            .padding(3.dp)
                            .background(screen.color)
                            .clickable {
                                navigationState.navController.navigate(screen.route.toString()) {
                                    popUpTo(navigationState.navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(screen.route.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationGraph(navigation: TopNavigation) {
    val state by navigation.viewModel.uiState.collectAsState()

    val firstItemRoute = state.topItems[1] ?: throw RuntimeException("route can`t be null")

    NavHost(navigation.navController as NavHostController, startDestination = firstItemRoute.route.toString()) {
        state.topItems.forEach { (route, item) ->
            composable(route.toString()) {
                if (navigation.viewModel.getShowResultState()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .padding(10.dp),
                                shape = RoundedCornerShape(15.dp),
                                elevation = CardDefaults.cardElevation(10.dp)
                            ) {
                                Row {
                                    Box(
                                        modifier = Modifier.padding(10.dp)
                                            .fillMaxSize()
                                            .weight(0.3f),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Column {
                                            Text(
                                                "Right answers",
                                                style = MaterialTheme.typography.titleMedium,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            Text(
                                                "${state.testResult.value.rightAnswers}",
                                                style = MaterialTheme.typography.titleMedium,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .fillMaxSize()
                                            .weight(0.3f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column {
                                            Text(
                                                "Wrong answers",
                                                style = MaterialTheme.typography.titleMedium,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            Text(
                                                "${state.testResult.value.wrongAnswers}",
                                                style = MaterialTheme.typography.titleMedium,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .fillMaxSize()
                                            .weight(0.3f),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Column {
                                            Text(
                                                "Percentage",
                                                style = MaterialTheme.typography.titleMedium,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            Text(
                                                "${state.testResult.value.correctAnswersPercentage} %",
                                                style = MaterialTheme.typography.titleMedium,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Box(modifier = Modifier.fillMaxWidth().weight(0.5f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Button(onClick = {
                                        navigation.viewModel.clearViewModel()
                                        navigation.activityContext.recreate()
                                    },
                                        colors = ButtonDefaults.buttonColors(Brown),
                                    ) {
                                        Text("start again")
                                    }
                                }

                                Box(modifier = Modifier.fillMaxWidth().weight(0.5f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Button(onClick = {
                                        val intent = Intent(navigation.activityContext, QuestionsResultActivity::class.java)
                                        intent.putExtra("test_result", objectMapper.writeValueAsString(state.resultItems.values))
                                        ContextCompat.startActivity(
                                            navigation.activityContext,
                                            intent,
                                            null
                                        )
                                    },
                                        colors = ButtonDefaults.buttonColors(Brown),
                                    ) {
                                        Text("show result")
                                    }
                                }
                            }
                        }
                    }
                } else if (navigation.viewModel.getSendResultButtonState()) {
                    Box(modifier = Modifier
                        .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { navigation.viewModel.sendAnswersForResult() },
                            colors = ButtonDefaults.buttonColors(Brown),
                        ) {
                            Text("send result")
                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item { Question(item) }

                        item.question.answers.forEach { answer ->
                            item { Answers(answer, navigation.viewModel, item.question, state) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Question(item: TopItem) {
    LazyRow {
        item {
            MarkdownText(markdown = item.question.content.trimIndent(), style = MaterialTheme.typography.titleMedium)
//                            MinimalExampleContent()
        }
    }
}

@Composable
fun Answers(answer: AnswerDomainDto, viewModel: QuestionActivityViewModel, question: QuestionDomainDto, state: TestActivityState) {
    Card(modifier = Modifier.padding(3.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (question.type == "one_answer") {
                RadioButton(
                    selected = state.answerSelected["${question.questionId} ${answer.answerId}"] ?: false,
                    onClick = {
                        viewModel.setChooseAnswerButton(question.questionId, answer.answerId)
                    },
                    modifier = Modifier
                        .size(20.dp)
                        .padding(start = 8.dp)
                )
            } else {
                Checkbox(
                    checked = state.answerSelected["${question.questionId} ${answer.answerId}"] ?: false,
                    onCheckedChange = {
                        viewModel.setChooseAnswerButton(question.questionId, answer.answerId)
                    },
                    modifier = Modifier
                        .size(20.dp)
                        .padding(start = 8.dp)
                )
            }

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
}


val markdownContent5 = """ 
        Given two files:
    
        ```
        1. package pkgA;
        2. public class Foo {
        3.   int a = 5;
        4.   protected int b = 6;
        5.   public int c = 7;
        6. }
        3. package pkgB;
        4. import pkgA.*;
        5. public class Baz {
        6.   public static void main(String[] args) {
        7.     Foo f = new Foo();
        8.     System.out.print(" " + f.a);
        9.     System.out.print(" " + f.b);
       10.     System.out.println(" " + f.c);
       11.   }
       12. }
       ```
       
       What is the result? (Choose all that apply.)
""".trimIndent()


@Composable
fun MinimalExampleContent() {
    MarkdownText(markdown = markdownContent5, style = MaterialTheme.typography.titleMedium)
}
