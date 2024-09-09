package com.example.workoutplanner.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.workoutplanner.model.ExerciseItem
import com.example.workoutplanner.model.TrainingCycle
import com.example.workoutplanner.model.Workout
import com.example.workoutplanner.ui.User
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WorkoutPlannerViewModel: ViewModel() {
   private val _uiState = MutableStateFlow(User())
   val uiState: StateFlow<User> = _uiState.asStateFlow()
   private val database = FirebaseDatabase.getInstance()
   val userReference = database.getReference("users")
   var user:User by mutableStateOf(User())

}