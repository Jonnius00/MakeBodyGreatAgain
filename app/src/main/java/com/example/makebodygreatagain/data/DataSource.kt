package com.example.makebodygreatagain.data

import android.content.Context
import com.example.makebodygreatagain.R

data class Exercise(
    val name: String,
    val description: String,
    val gifUrl: String = "https://c.tenor.com/vADKbpqeAtYAAAAd/tenor.gif",
    val sets: Int,
    var setsCompleted: Int = 0,
    var isExpanded: Boolean = false
)

data class TrainingProgram(
    val name: String,
    val exercises: List<Exercise>
)

class DataSource(context: Context) {
    val strengthExercises = listOf(
        Exercise(context.getString(R.string.exercise_bench_press), context.getString(R.string.exercise_bench_press_desc), context.getString(R.string.gif_url1), 4),
        Exercise(context.getString(R.string.exercise_shoulder_press), context.getString(R.string.exercise_shoulder_press_desc), context.getString(R.string.gif_url2), 4),
        Exercise(context.getString(R.string.exercise_deadlift), context.getString(R.string.exercise_deadlift_desc), context.getString(R.string.gif_url3), 4),
        Exercise(context.getString(R.string.exercise_squats), context.getString(R.string.exercise_squats_desc), context.getString(R.string.gif_url4), 4)
    )

    val enduranceExercises = listOf(
        Exercise(context.getString(R.string.exercise_running), context.getString(R.string.exercise_running_desc), context.getString(R.string.gif_url5), 1),
        Exercise(context.getString(R.string.exercise_cycling), context.getString(R.string.exercise_cycling_desc), context.getString(R.string.gif_url6), 1),
        Exercise(context.getString(R.string.exercise_swimming), context.getString(R.string.exercise_swimming_desc), context.getString(R.string.gif_url7), 1),
        Exercise(context.getString(R.string.exercise_rowing), context.getString(R.string.exercise_rowing_desc), context.getString(R.string.gif_url8), 1)
    )

    val programs = listOf(
        TrainingProgram(context.getString(R.string.program_strength), strengthExercises),
        TrainingProgram(context.getString(R.string.program_endurance), enduranceExercises)
    )
}
