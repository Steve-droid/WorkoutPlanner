package com.example.workoutplanner

data class Exercise(
   val name:String,
   val type:String,
   val muscle:String,
   val equipment:String,
   val difficulty:String,
   val instructions:String,
   val sets:Int = 0,
   val reps:Int = 0
)

data class Workout(
   var exerciseList: List<Exercise> = emptyList(),
   val workoutName:String? = null,
   val dayOfWeek:Int = 0
)

data class TrainingCycle(
   val workoutList: List<Workout> = emptyList(),
   val cycleName:String? = null,
   val cycleDescription:String? = null,
   val numberOfWeeks:Int = 0
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
