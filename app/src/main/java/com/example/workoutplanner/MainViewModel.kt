package com.example.workoutplanner

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

   private val _exerciseState = mutableStateOf(ExerciseState())
   val exerciseState: State<ExerciseState> = _exerciseState

   init {
      fetchExercises()
   }

  fun fetchExercises(muscleGroup: String = ""){
      viewModelScope.launch {
         try{
             val exercises = exerciseService.getExercises(muscleGroup)
            _exerciseState.value = _exerciseState.value.copy(
               list = exercises,
               loading = false,
               error = null
            )
         }
         catch (e: Exception){
            _exerciseState.value = _exerciseState.value.copy(
               loading = false,
               error = "Error while fetching Exercises. Error message: ${e.message}"

            )

         }
      }
   }

   data class ExerciseState(
      val loading:Boolean = true,
      val editingSearchTerm: Boolean = false,
      val list: List<Exercise> = emptyList(),
      val error: String? = null
   )
}