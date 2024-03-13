package com.example.cert.ui.activity.question

import android.content.Context
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cert.domain.model.AnswerDomainDto
import com.example.cert.domain.model.QuestionDomainDto
import com.example.cert.ui.model.TestActivityState
import com.example.cert.ui.model.TopItem
import com.example.cert.ui.model.TopNavigation
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
                        QuestionMainScreen(navController, viewModel, activityContext)
                    }
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)) {
                        BottomMenu(navController, viewModel)
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
fun QuestionMainScreen(navController: NavController, viewModel: QuestionActivityViewModel, activityContext: Context) {
    val navigation = TopNavigation(navController = navController, viewModel = viewModel, activityContext = activityContext)
    Scaffold(
        modifier = Modifier
            .background(Color.Yellow)
            .fillMaxWidth(),
        topBar = {
            TopNavigation(navigation)
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
fun BottomMenu(navController: NavController, viewModel: QuestionActivityViewModel) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.toInt()
    val state by viewModel.uiState.collectAsState()
    val questionsSize = state.questions?.questions?.size ?: 0
    val prevRote = if (currentRoute != null && currentRoute > 1) { currentRoute - 1 } else { 1 }
    val nextRote = if (currentRoute != null && currentRoute < questionsSize) { currentRoute + 1 } else { questionsSize }

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
        ) {
            Text("prev")
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Button(
            onClick = { /*TODO*/ },
        ) {
            Text("commit answer")
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
        ) {
            Text("next")
        }
    }
}

@Composable
fun TopNavigation(navigationState: TopNavigation) {
    val navBackStackEntry by navigationState.navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val state by navigationState.viewModel.uiState.collectAsState()
    val items = state.questions?.questions?.map { TopItem(route = it.questionId, question = it) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .padding(5.dp),
    ) {
        item {
            items?.forEach { screen ->
                if (currentRoute == screen.route.toString()) {
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .padding(5.dp)
                            .background(Color.DarkGray)
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
                            .background(Color.LightGray)
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
    val items = state.questions?.questions?.map { TopItem(route = it.questionId, question = it) }

    val firstItemRoute = items?.firstOrNull()?.route?.toString() ?: throw RuntimeException("route can`t be null")

    NavHost(navigation.navController as NavHostController, startDestination = firstItemRoute) {
        items.forEach { item ->
            composable(item.route.toString()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item { Question(item) }

                    item.question.answers.forEach { answer ->
                        item { Answers(answer, navigation.viewModel, item.question) }
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
fun Answers(answer: AnswerDomainDto, viewModel: QuestionActivityViewModel, question: QuestionDomainDto) {
    val state by viewModel.uiState.collectAsState()

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