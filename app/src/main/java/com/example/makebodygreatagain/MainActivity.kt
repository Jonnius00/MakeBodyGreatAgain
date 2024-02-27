package com.example.makebodygreatagain

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import com.example.makebodygreatagain.data.DataSource
import com.example.makebodygreatagain.data.Exercise
import com.example.makebodygreatagain.ui.theme.MakeBodyGreatAgainTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MakeBodyGreatAgainTheme {
                BodyBuilding()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MakeBodyGreatAgainTheme {
        BodyBuilding()
    }
}

@Composable
fun BodyBuilding() {
    // This state determines which exercises to display
    var exerciseType by remember { mutableStateOf(ExerciseType.Endurance) }

    Column {
        Row {
            Button(onClick = { exerciseType = ExerciseType.Endurance }) {
                Text(text = "Endurance")
            }
            Button(onClick = { exerciseType = ExerciseType.Strength }) {
                Text(text = "Strength")
            }
        }

        // Pass the exercises based on the current exercise type without reinitializing them
        MyLayout(exerciseType, globalExercisesState.value[exerciseType]!!)
    }
}


val globalExercisesState = mutableStateOf(
    mapOf(
        ExerciseType.Endurance to DataSource.enduranceExercises.map { mutableStateOf(it) }.toMutableStateList(),
        ExerciseType.Strength to DataSource.strengthExercises.map { mutableStateOf(it) }.toMutableStateList()
    )
)

enum class ExerciseType {
    Endurance,
    Strength
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLayout(exerciseType: ExerciseType, exercisesState: List<MutableState<Exercise>>) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My New Body") })
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                ExerciseCounters(exercises = exercisesState)
                ExerciseList(exerciseState = exercisesState)
            }
        }
    )
}

@Composable
fun ExerciseCounters(exercises: List<MutableState<Exercise>>) {
    val totalSets = exercises.sumOf { it.value.sets }
    val completedSets = exercises.sumOf { it.value.setsCompleted }
    val completedExercises = exercises.count { it.value.setsCompleted == it.value.sets }

    Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Total Sets: $completedSets / $totalSets")
        Text("Completed Exercises: $completedExercises / ${exercises.size}")
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
