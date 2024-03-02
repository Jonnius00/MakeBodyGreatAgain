package com.example.makebodygreatagain

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.makebodygreatagain.data.DataSource
import com.example.makebodygreatagain.data.Exercise
import com.example.makebodygreatagain.ui.theme.MakeBodyGreatAgainTheme
import com.example.makebodygreatagain.data.TrainingProgram

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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BodyBuildingScreen()  // This should call BodyBuildingScreen to ensure context is available
}

@Composable
fun BodyBuilding(globalExercisesState: Map<ExerciseType, List<MutableState<Exercise>>>) {
    var exerciseType by remember { mutableStateOf(ExerciseType.Endurance) }

    // Pass the exercises based on the current exercise type without reinitializing them
    val exercisesState = globalExercisesState[exerciseType] ?: emptyList()
    MyLayout(exerciseType = exerciseType, exercisesState = exercisesState, onExerciseTypeChange = { exerciseType = it })
}


enum class ExerciseType {
    Endurance,
    Strength
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLayout(exerciseType: ExerciseType, exercisesState: List<MutableState<Exercise>>, onExerciseTypeChange: (ExerciseType) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.top_bar_title), modifier = Modifier.fillMaxWidth(), // Ensures the Text composable fills the available width
                textAlign = TextAlign.Center) })
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = { onExerciseTypeChange(ExerciseType.Endurance) },
                    modifier = Modifier.testTag("EnduranceButton"),
                    shape = RoundedCornerShape(3.dp) // Making the button rectangular
                ) {
                    Text(text = stringResource(id = R.string.exercise_type_endurance))
                }
                Button(
                    onClick = { onExerciseTypeChange(ExerciseType.Strength) },
                    modifier = Modifier.testTag("StrengthButton"),
                    shape = RoundedCornerShape(3.dp) // Making the button rectangular
                ) {
                    Text(text = stringResource(id = R.string.exercise_type_strength))
                }
            }

            ExerciseCounters(exercises = exercisesState)
            ExerciseList(exerciseState = exercisesState)
        }
    }
}


@Composable
fun ExerciseCounters(exercises: List<MutableState<Exercise>>) {
    val totalSets = exercises.sumOf { it.value.sets }
    val completedSets = exercises.sumOf { it.value.setsCompleted }
    val completedExercises = exercises.count { it.value.setsCompleted == it.value.sets }

    // Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween)
    Column (modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.Start)
    {
        Text(stringResource(R.string.total_sets, completedSets, totalSets))
        Text(stringResource(R.string.completed_exercises, completedExercises, exercises.size))
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = exercise.name)
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Outlined.ArrowDropDown else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand"
                    )
                }
            }
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = exercise.description)
                    for (i in 1..exercise.sets) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Set $i")
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
                }
            }
        }
    }
}
