package com.example.makebodygreatagain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable

//import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.ArrowDropDown

import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList

import com.example.makebodygreatagain.data.DataSource
import com.example.makebodygreatagain.data.Exercise
import com.example.makebodygreatagain.ui.theme.MakeBodyGreatAgainTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MakeBodyGreatAgainTheme {
                // A surface container using the 'background' color from the theme
                /* Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BodyBuilding()
                } */
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
    // This state list will hold the state for each exercise, which gets passed down to the ExerciseList
    val exercisesState = remember { DataSource.strengthExercises.map { mutableStateOf(it) }.toMutableStateList() }

    Scaffold(
        topBar = {
            modifier = Modifier,
            TopAppBar(
                title = { Text("My New Body") }
            )
        },
    ) content = {  paddingValues ->
        Column (
            modifier = Modifier.padding(paddingValues)
        ){
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

    // Display the counters
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
        elevation = 4.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = exercise.name,
                    //style = MaterialTheme.typography.h6
                )
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Outlined.ArrowDropDown else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand"
                    )
                }
            }
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = exercise.description,
                        //style = MaterialTheme.typography.body2
                    )
                    // Sets UI
                    for (i in 1..exercise.sets) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Set $i")
                            Spacer(modifier = Modifier.width(8.dp))
                            Switch(
                                checked = exercise.setsCompleted >= i,
                                onCheckedChange = { isChecked ->
                                    // Update state based on switch change
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