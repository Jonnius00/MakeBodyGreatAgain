package com.example.makebodygreatagain.data

data class Exercise(
    val name: String,
    val description: String,
    val videoUrl: String,
    val sets: Int,
    var setsCompleted: Int = 0,
    var isExpanded: Boolean = false // default to collapsed
)

data class TrainingProgram(
    val name: String,
    val exercises: List<Exercise>
)

object DataSource {
    val strengthExercises = listOf(
        Exercise("Bench Press", "Perform 10 repetitions with increasing weight.", "https://uit.no/ivt", 4),
        Exercise("Shoulder Press", "Perform 12 repetitions with light weights.", "https://uit.no/ivt", 4),
        // Add more exercises here...
    )

    val enduranceExercises = listOf(
        Exercise("Running", "30 minutes continuous running at a moderate pace.", "https://uit.no/ivt", 1),
        Exercise("Cycling", "45 minutes of interval training on a stationary bike.", "https://uit.no/ivt", 1),
        // Add more exercises here...
    )

    val programs = listOf(
        TrainingProgram("Strength", strengthExercises),
        TrainingProgram("Endurance", enduranceExercises)
    )
}

