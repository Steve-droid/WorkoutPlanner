package com.example.workoutplanner.viewmodel

import androidx.compose.runtime.MutableState

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutplanner.ExerciseItem
import com.example.workoutplanner.User
import com.example.workoutplanner.Workout
import com.example.workoutplanner.exerciseService
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.system.exitProcess

data class UIState(
   var workoutList: MutableState<List<Workout>> = mutableStateOf(listOf()),
   var exerciseListCatalog: MutableState<List<ExerciseItem>> = mutableStateOf(listOf(ExerciseItem())),
   var newWorkoutDay:MutableState<String> = mutableStateOf(""),
   var currentWorkout: MutableState<Workout> = mutableStateOf(Workout()),
   var newWorkout: MutableState<Workout> = mutableStateOf(Workout()),
   var currentExercise: MutableState<ExerciseItem> = mutableStateOf(ExerciseItem()),
   var newExercise: MutableState<ExerciseItem> = mutableStateOf(ExerciseItem()),
   var user: MutableState<User> = mutableStateOf(User("","")),
   var menuExpanded: MutableState<Boolean> = mutableStateOf(false),
   var exerciseSelected:MutableState<Boolean> = mutableStateOf(false),
   var loading:MutableState<Boolean> = mutableStateOf(false),
   var error: MutableState<Boolean> = mutableStateOf(false),
   var searchBar: MutableState<String> = mutableStateOf(""),
   val dayOptions: List<String> = listOf(
      "Monday",
      "Tuesday",
      "Wednesday",
      "Thursday",
      "Friday",
      "Saturday",
      "Sunday"
   ),
   var nextWorkoutID: MutableState<Int> = mutableIntStateOf(1)
)

class CreateCycleViewModel : ViewModel() {
   private var _UIState = mutableStateOf(UIState())
   var uiState: MutableState<UIState> = _UIState

   init {

      uiState.value.user.value.userId  = UUID.randomUUID().toString()
      uiState.value.workoutList.value =
             mutableListOf(
                Workout(day = "Sunday", workoutID = 0),
                Workout(day ="Monday", workoutID = 1),
                Workout(day ="Tuesday", workoutID = 2),
                Workout(day ="Wednesday", workoutID = 3),
                Workout(day ="Thursday", workoutID = 4),
                Workout(day ="Friday", workoutID = 5),
                Workout(day ="Saturday", workoutID = 6)
             )

      uiState.value.currentWorkout.value = uiState.value.workoutList.value[0]
      uiState.value.newWorkoutDay = mutableStateOf(uiState.value.currentWorkout.value.day)
   }
   // Mutable state list of workout days
   fun addExerciseToWorkout(
      userID: String = "",
      cycleID: String="",
      workoutID: String="",
      exercise: ExerciseItem,
      workout: Workout
   ){
      let{
         workout.exercises.value += exercise
      }
      val databaseReference = FirebaseDatabase
         .getInstance()
         .getReference(
            "users/" +
                   "$userID/" +
                   "cycles/" +
                   "$cycleID/" +
                   "workouts/" +
                   "$workoutID/" +
                   "exercises")
      val exerciseID = databaseReference.push().key

      databaseReference.child(exerciseID!!).setValue(exercise).addOnCompleteListener {
         if(it.isCanceled){
          exitProcess(-1)
         }
      }
   }



   fun fetchExercises(){
      viewModelScope.launch {
         try{
            val newExerciseList = exerciseService.getExercises(uiState.value.searchBar.value)
            uiState.value.exerciseListCatalog.value = newExerciseList

         }
         catch (e: Exception){
            uiState.value.error.value = true
            uiState.value.loading.value = false
         }
      }
   }

   fun onClickAddExercise(
      onNavigateToCatalog: () -> Unit,
   ) {
      onNavigateToCatalog()
   }

   fun onSelectExercise(onNavigateToCreateCycleScreen: () -> Unit){
      var workoutToAddTo = uiState.value.workoutList.value.find {
         it.day == uiState.value.newWorkoutDay.value
      }

      workoutToAddTo?.exercises?.value = workoutToAddTo?.exercises?.value!! + uiState.value.newExercise.value
      onNavigateToCreateCycleScreen()
   }

   fun removeExercise( exercise: ExerciseItem,workout: Workout) {
      workout.exercises.value -= exercise
   }

   fun updateWorkoutDay(workout: Workout, newDay: String) {

     workout.day = newDay
   }
}