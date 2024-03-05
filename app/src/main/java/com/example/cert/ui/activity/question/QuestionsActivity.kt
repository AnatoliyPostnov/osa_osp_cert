package com.example.cert.ui.activity.question

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cert.R
import com.example.cert.domain.model.QuestionsForTestingDomainDto
import com.example.cert.ui.activity.MainActivity
import com.example.cert.ui.model.TopItem
import com.example.cert.ui.utils.getAppComponent
import com.example.cert.ui.viewmodel.Factory
import com.example.cert.ui.viewmodel.QuestionActivityViewModel
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
            val uiState by viewModel.uiState.collectAsState()
            val activityContext = this
            val themeId = activityContext.intent.extras?.getInt("theme_id")

            viewModel.getQuestionsByThemeId(activityContext, themeId)
            val questions = uiState.questions

            if (questions == null) {
                Text(text = "questions were not found")
            } else {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)) {
                        Column {
                            Text(questions.themeContent, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        }
                    }

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(5f)) {
                        MainScreen(questions)
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
                                        val intent =
                                            Intent(activityContext, MainActivity::class.java)
                                        ContextCompat.startActivity(
                                            activityContext,
                                            intent,
                                            null
                                        )
                                    }
                            )
                            Text("back", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(questions: QuestionsForTestingDomainDto) {
    val navController = rememberNavController()
    val items = questions.questions.map { TopItem(route = it.questionId) }
    Scaffold(
        modifier = Modifier
            .background(Color.Yellow)
            .fillMaxWidth(),
        topBar = {
            TopNavigation(navController, items)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            NavigationGraph(navController, items)
        }

    }
}

@Composable
fun TopNavigation(navController: NavController, items: List<TopItem>) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .padding(5.dp)
            .horizontalScroll(rememberScrollState())
        ,
        containerColor = Color.Yellow
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route.toString(),
                onClick = {
                    navController.navigate(screen.route.toString()) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Box(
                    modifier = Modifier
                    .size(30.dp)
                    .background(Color.DarkGray)
                ) }
            )
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, items: List<TopItem>) {
    val firstItemRoute = items.first().route.toString()

    NavHost(navController, startDestination = firstItemRoute) {
        items.forEach {item ->
            composable(item.route.toString()) {
                Text(text = item.toString())
            }
        }
    }
}