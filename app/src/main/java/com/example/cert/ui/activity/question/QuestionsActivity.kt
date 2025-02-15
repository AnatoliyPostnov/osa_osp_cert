package com.example.cert.ui.activity.question

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cert.R
import com.example.cert.configuration.JacksonConfig.Companion.objectMapper
import com.example.cert.domain.model.AnswerDomainDto
import com.example.cert.domain.model.QuestionDomainDto
import com.example.cert.ui.activity.MainActivity
import com.example.cert.ui.model.ResultItem
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
            val themeId = activityContext.intent.extras?.getLong("theme_id")?.toInt()
            val examId = activityContext.intent.extras?.getLong("exam_id")?.toInt()

            viewModel.findQuestionsByThemeIdAndExamId(activityContext, themeId, examId)

            val state by viewModel.uiState.collectAsState()

            val navController = rememberNavController()

            if (state.questions == null) {
                Text(text = "questions were not found")
            } else {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box {
                        Header(state, Modifier.align(Alignment.TopCenter))
                        QuestionMainScreen(navController, viewModel, activityContext, state, Modifier.align(Alignment.Center))
                        BottomMenu(navController, viewModel, Modifier.align(Alignment.BottomCenter))
                    }
                }
            }
        }
    }
}

@Composable
fun Header(state: TestActivityState, modifier: Modifier) {
    Text(
        text = state.questions?.themeContent ?: "Content was not found",
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun QuestionMainScreen(navController: NavController, viewModel: QuestionActivityViewModel, activityContext: QuestionsActivity, state: TestActivityState, modifier: Modifier) {
    val navigation = TopNavigation(navController = navController, viewModel = viewModel, activityContext = activityContext)
    Scaffold(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 50.dp, top = 30.dp),
        topBar = {
            TopNavigation(navigation, state)
        },

    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            NavigationGraph(navigation)
        }
    }
}

@Composable
fun BottomMenu(navController: NavController, viewModel: QuestionActivityViewModel, modifier: Modifier) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.toInt()
    val nextRote = viewModel.getNextRoute(currentRoute)
    val prevRote = viewModel.getPrevRoute(currentRoute)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = {
                if (!viewModel.commitQuestion(currentRoute)) {
                    viewModel.uncommittedQuestion(currentRoute)
                }
                navController.navigate(prevRote.toString()) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = ButtonDefaults.buttonColors(Brown),
            modifier = Modifier
                .width(150.dp)
                .height(50.dp)
        ) {
            Text("prev")
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Button(
            onClick = {
                if (!viewModel.commitQuestion(currentRoute)) {
                    viewModel.uncommittedQuestion(currentRoute)
                }
                navController.navigate(nextRote.toString()) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = ButtonDefaults.buttonColors(Brown),
            modifier = Modifier
                .width(150.dp)
                .height(75.dp)
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
            .height(44.dp),
    ) {
        item {
            state.topItems.forEach { (route, screen) ->
                if (currentRoute == route.toString()) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(2.dp)
                            .background(Color.DarkGray)
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
                    ){
                        Text(screen.route.toString())
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(2.dp)
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

    NavHost(
        navigation.navController as NavHostController,
        startDestination = firstItemRoute.route.toString(),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }) {
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
                                        modifier = Modifier
                                            .padding(10.dp)
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
                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.5f),
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

                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.5f),
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
                        .size(30.dp)
                        .padding(start = 8.dp)
                )
            } else {
                Checkbox(
                    checked = state.answerSelected["${question.questionId} ${answer.answerId}"] ?: false,
                    onCheckedChange = {
                        viewModel.setChooseAnswerButton(question.questionId, answer.answerId)
                    },
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 8.dp)
                )
            }
            LazyRow {
                item {
                    MarkdownText(
                        markdown = "${answer.answerId}. ${answer.content}".trimIndent(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        onClick = {
                            viewModel.setChooseAnswerButton(question.questionId, answer.answerId)
                        },
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}


val markdownContent5 = """ 
Given:
```
try (Connection conn = DriverManager.getConnection(url, username, password)) { 
    conn.setAutoCommit(false);
    String q1, q2, q3;
    q1 = "INSERT INTO Order VALUES(23, 99.99, 'Winter Boots')";
    q2 = "INSERT INTO Order VALUES(24, 39.99, 'Fleece Jacket')"; 
    q3 = "INSERT INTO Order VALUES(25, 29.99, 'Wool Scarf')"; 
    Statement stmt = conn.createStatement(); 
    stmt.executeUpdate(q1);
    Savepoint sp1 = conn.setSavepoint("item1"); 
    stmt.executeUpdate(q2);
    Savepoint sp2 = conn.setSavepoint("item2"); 
    conn.rollback(sp1);
    stmt.executeUpdate(q3);
    Savepoint sp3 = conn.setSavepoint("item3"); 
    conn.commit();
} catch (SQLException se) { 
    System.out.println ("SQLException");
}
```
Assuming that the Order table was empty before this code 
fragment was executed and that the database supports 
multiple savepoints and that all of the queries are 
valid, what rows does Order contain?
""".trimIndent()


@Composable
fun MinimalExampleContent() {
    MarkdownText(markdown = markdownContent5, style = MaterialTheme.typography.titleMedium)
}

@Composable
fun MinimalExampleContentPreview() {
    MarkdownText(markdown = markdownContent5, style = MaterialTheme.typography.titleMedium)
}

@Preview(showBackground = true)
@Composable
fun QuestionPreview() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        LazyRow {
//            item {
////            MarkdownText(markdown = item.question.content.trimIndent(), style = MaterialTheme.typography.titleMedium)
//                MinimalExampleContentPreview()
//            }
//        }
//    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(Color.White)) {

            items(
                items = listOf(ResultItem(
                    id = 0,
                    isRight = true,
                    yourAnswer = "yourAnswer",
                    rightAnswer = "yourAnswer",
                    explanation = "yourAnswer",
                    question = "yourAnswer",
                    answers = listOf(AnswerDomainDto(1, "fdfdf", true))
                )),
                itemContent = { itm ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(850.dp)
                            .padding(10.dp),
                        shape = RoundedCornerShape(15.dp),
                        elevation = CardDefaults.cardElevation(10.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier
                                .weight(0.93f)
//                                .padding(15.dp, end = 0.dp)
                            ) {
                                Text(
                                    text = "Your answer: ${itm.yourAnswer}",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Start
                                )
                                Text(
                                    text = "Correct answer: ${itm.rightAnswer}",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Start
                                )
                                Text(
                                    text = "Explanation: ${itm.explanation}",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Start
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(0.07f)
                                    .fillMaxSize()
                                    .background(
                                        colorResource(R.color.light_green)

//                                        if (itm.isRight) {
//                                            colorResource(R.color.light_green)
//                                        } else {
//                                            colorResource(R.color.light_red)
//                                        }
                                    )
                            )
                        }
                    }
                })
        }
    }
}