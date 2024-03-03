package com.example.makebodygreatagain

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.makebodygreatagain.data.DataSource
import com.example.makebodygreatagain.data.Exercise
import com.example.makebodygreatagain.ui.theme.MakeBodyGreatAgainTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MakeBodyGreatAgainTheme {
                BodyBuildingScreen()
            }
        }
    }
}

@Composable
fun BodyBuildingScreen() {
    val context = LocalContext.current
    val dataSource = remember { DataSource(context) }

    val globalExercisesState = remember {
        mutableStateOf(
            mapOf(
                ExerciseType.Endurance to dataSource.enduranceExercises.map { mutableStateOf(it) }.toMutableStateList(),
                ExerciseType.Strength to dataSource.strengthExercises.map { mutableStateOf(it) }.toMutableStateList()
            )
        )
    }

    BodyBuilding(globalExercisesState = globalExercisesState.value)
}

@Composable
fun BodyBuilding(globalExercisesState: Map<ExerciseType, List<MutableState<Exercise>>>) {
    var exerciseType by remember { mutableStateOf(ExerciseType.Endurance) }

    MyLayout(
        exerciseType = exerciseType,
        exercisesState = globalExercisesState,
        onExerciseTypeChange = { newExerciseType ->
            exerciseType = newExerciseType
        }
    )
}


enum class ExerciseType {
    Endurance,
    Strength
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLayout(
    exerciseType: ExerciseType,
    exercisesState: Map<ExerciseType, List<MutableState<Exercise>>>,
    onExerciseTypeChange: (ExerciseType) -> Unit
) {
    Scaffold(
        topBar = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                AnimatedGifWebView(gifUrl = "https://c.tenor.com/T4fluv4fprIAAAAC/tenor.gif", modifier = Modifier.width(63.dp))

                TopAppBar(title = {
                    Text(stringResource(id = R.string.top_bar_title),
                        textAlign = TextAlign.Center) })
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
            // Track the currently selected exercise type
            var selectedExerciseType by remember { mutableStateOf(exerciseType) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        selectedExerciseType = ExerciseType.Endurance
                        onExerciseTypeChange(selectedExerciseType)
                    },
                    modifier = Modifier
                        .testTag("EnduranceButton")
                        .width(185.dp),
                    shape = RoundedCornerShape(3.dp),
                    enabled = selectedExerciseType != ExerciseType.Endurance
                ) {
                    Text(text = stringResource(id = R.string.exercise_type_endurance))
                }
                Button(
                    onClick = {
                        selectedExerciseType = ExerciseType.Strength
                        onExerciseTypeChange(selectedExerciseType)
                    },
                    modifier = Modifier
                        .testTag("StrengthButton")
                        .width(185.dp),
                    shape = RoundedCornerShape(3.dp),
                    enabled = selectedExerciseType != ExerciseType.Strength
                ) {
                    Text(text = stringResource(id = R.string.exercise_type_strength))
                }
            }

            // Display the exercises for the selected type
            val currentExercises = exercisesState[selectedExerciseType] ?: emptyList()
            ExerciseCounters(exercises = currentExercises)
            ExerciseList(exerciseState = currentExercises)
        }
    }
}


@Composable
fun AnimatedGifWebView(modifier: Modifier = Modifier, gifUrl: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()

                // Enable JavaScript if needed
                // settings.javaScriptEnabled = true

                // GIF within an HTML string to control its size
                val htmlString = """
                    <html>
                    <head>
                        <style type="text/css">
                            body { margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; }
                            img { width: 100%; } /* Adjust the width as needed */
                        </style>
                    </head>
                    <body>
                        <img src="$gifUrl" />
                    </body>
                    </html>
                """.trimIndent()

                loadDataWithBaseURL(null, htmlString, "text/html", "UTF-8", null)
            }
        },
        modifier = modifier
    )
}


@Composable
fun ExerciseCounters(exercises: List<MutableState<Exercise>>) {
    val totalSets = exercises.sumOf { it.value.sets }
    val completedSets = exercises.sumOf { it.value.setsCompleted }
    val completedExercises = exercises.count { it.value.setsCompleted == it.value.sets }

    // Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween)
    Column (modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.Start)
    {
        Text(
            stringResource(R.string.total_sets, completedSets, totalSets),
            modifier = Modifier.testTag("TotalSetsText"))
        Text(
            stringResource(R.string.completed_exercises, completedExercises, exercises.size),
            modifier = Modifier.testTag("CompletedExercisesText"))
    }
}

@Composable
fun ExerciseList(exerciseState: List<MutableState<Exercise>>) {
    LazyColumn {
        items(exerciseState) { state ->
            ExerciseCard(exercise = state.value, onExerciseChanged = { updatedExercise ->
                state.value = updatedExercise
            })
        }
    }
}

@Composable
fun ExerciseCard(exercise: Exercise, onExerciseChanged: (Exercise) -> Unit) {
    var isExpanded by remember { mutableStateOf(exercise.isExpanded) }
    var isIconVisible by remember { mutableStateOf(true) } // Controls visibility for the animated icon
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { isExpanded = !isExpanded },
        //elevation = 4.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Animated icon to the left of the exercise name
                AnimatedVisibility(
                    visible = isIconVisible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    // This is where your animated icon will be
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder, // Replace with your preferred icon
                        contentDescription = "Exercise Icon",
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text

                // Exercise name and clickable text for URL
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = exercise.name)
                    Text(
                        text = "Video",
                        modifier = Modifier.clickable {
                            uriHandler.openUri("https://uit.no/startsida") // URL you want to open
                        },
                    )
                }

                // Expand/collapse icon button
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Outlined.ArrowDropDown else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand"
                    )
                }
            }

            // Expandable content with exercise description and sets
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = exercise.description)
                    for (i in 1..exercise.sets) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = stringResource(id = R.string.set_number, i))
                            Spacer(modifier = Modifier.width(8.dp))
                            Switch(
                                checked = exercise.setsCompleted >= i,
                                onCheckedChange = { isChecked ->
                                    val updatedExercise = exercise.copy(setsCompleted = if (isChecked) i else i - 1)
                                    onExerciseChanged(updatedExercise)
                                }
                            )
                        }
                    }
                    // Load the GIF using a WebView for animation
                    if (exercise.gifUrl.isNotEmpty()) {
                        AnimatedGifWebView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp), // Set a fixed height for the GIF
                            gifUrl = exercise.gifUrl
                        )
                        }
                    }
                }
            }
        }
    }



