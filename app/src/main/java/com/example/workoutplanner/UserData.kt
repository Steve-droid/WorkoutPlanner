package com.example.workoutplanner

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


data class ExerciseItem(
   val exerciseID: String = "",
   var name: String = "",
   val type: String = "",
   var muscle: String = "",
   val equipment: String = "",
   val difficulty: String = "",
   val instructions: String = "",
   val sets: String = "",
   val reps: String = ""
)

data class Workout(
   val workoutID: Int = 0,
   var exercises: MutableState<List<ExerciseItem>> = mutableStateOf(listOf<ExerciseItem>()),
   var day: String = "",
   val name: String = ""
)

data class TrainingCycle(
   val cycleID: String = "",
   val workoutList: List<Workout> = emptyList(),
   val cycleName: String? = null,
   val cycleDescription: String? = null,
   val numberOfWeeks: Int = 0
)


data class ExerciseState(
   val loading: Boolean = true,
   val editingSearchTerm: Boolean = false,
   var list: List<ExerciseItem> = emptyList(),
   val error: String? = null
)


/*
  {
    "name": "Incline Hammer Curls",
    "type": "strength",
    "muscle": "biceps",
    "equipment": "dumbbell",
    "difficulty": "beginner",
    "instructions": "Seat yourself on an incline bench with a dumbbell in each hand. You should pressed firmly against he back with your feet together. Allow the dumbbells to hang straight down at your side, holding them with a neutral grip. This will be your starting position. Initiate the movement by flexing at the elbow, attempting to keep the upper arm stationary. Continue to the top of the movement and pause, then slowly return to the start position."
  },


 */