package com.example.workoutplanner

sealed interface SignUpResult {
   data class Success(val username: String) : SignUpResult
   data object Failure : SignUpResult
   data object Cancelled : SignUpResult
}