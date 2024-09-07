package com.example.workoutplanner.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
   private val _welcomeMessage = mutableStateOf("Welcome to the Workout Planner")
   val welcomeMessage: State<String> = _welcomeMessage

   init {
      // Initialize or load data here
      loadWelcomeMessage()
   }

   private fun loadWelcomeMessage() {
      viewModelScope.launch {
         // Simulate loading data
         _welcomeMessage.value = "What would you like to do?"
      }
   }
}