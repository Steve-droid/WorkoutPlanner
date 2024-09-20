package com.example.workoutplanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class SharedViewModel : ViewModel() {
   private val _uiState = MutableStateFlow(UIState())
   val uiState: StateFlow<UIState> = _uiState

   private val auth = FirebaseAuth.getInstance()
   private val db = FirebaseFirestore.getInstance()

   fun updateSearchBarQuery(newQuery: String) {
      uiState.value.searchBar.value = newQuery
   }

   fun updateWorkoutDay(workout: WorkoutState, onNavigateToCatalog: (String) -> Unit) {
      uiState.value.newWorkoutDay.value = workout.day
      onNavigateToCatalog(workout.day)
   }

   fun updateCurrentWorkout(newWorkout: WorkoutState) {
      uiState.value.newWorkout.value = newWorkout

   }

   fun getSearchBarQuery(): String {
      return uiState.value.searchBar.value
   }

   fun createTrainingCycle(
      cycleName: String,
      daysPerWeek: Int,
      numberOfWeeks: Int,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
   ) {
      viewModelScope.launch {
         try {
            val uid = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

            val workouts = List(daysPerWeek) { index ->
               WorkoutState(
                  id = UUID.randomUUID().toString(),
                  day = getDayName(index),
                  name = "Workout ${index + 1}",
                  exercises = emptyList()
               )
            }

            val cycle = FirestoreTrainingCycle(
               id = UUID.randomUUID().toString(),
               cycleName = cycleName,
               numberOfWeeks = numberOfWeeks
            )

            // Update the UI state
            _uiState.update { currentState ->
               currentState.copy(
                  currentCycle = cycle,
                  workouts = workouts
               )
            }

            // Save to Firestore (you might want to do this after the user finishes adding exercises)
            // saveTrainingCycleToFirestore(uid, cycle, workouts)

            onSuccess()
         } catch (e: Exception) {
            onError("Failed to create training cycle: ${e.message}")
         }
      }
   }

   fun addExerciseToWorkout(workoutId: String, exercise: ExerciseItem) {
      _uiState.update { currentState ->
         val updatedWorkouts = currentState.workouts.map { workout ->
            if (workout.id == workoutId) {
               workout.copy(exercises = workout.exercises + exercise)
            } else {
               workout
            }
         }
         currentState.copy(workouts = updatedWorkouts)
      }
   }

   fun saveCompletedTrainingCycle(
      onSuccess: () -> Unit,
      onError: (String) -> Unit
   ) {
      viewModelScope.launch {
         try {
            val uid = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
            saveTrainingCycleToFirestore(uid, _uiState.value.currentCycle, _uiState.value.workouts)
            onSuccess()
         } catch (e: Exception) {
            onError("Failed to save training cycle: ${e.message}")
         }
      }
   }

   private suspend fun saveTrainingCycleToFirestore(
      userId: String,
      cycle: FirestoreTrainingCycle,
      workouts: List<WorkoutState>
   ) {
      val cycleRef = db.collection("users").document(userId)
         .collection("trainingCycles").document(cycle.id)

      cycleRef.set(cycle).await()

      workouts.forEach { workout ->
         val workoutRef = cycleRef.collection("workouts").document(workout.id)
         workoutRef.set(workout).await()

         workout.exercises.forEach { exercise ->
            workoutRef.collection("exercises").add(exercise).await()
         }
      }
   }


   fun fetchExercises() {
      viewModelScope.launch {
         try {
            val newExerciseList = exerciseService.getExercises(uiState.value.searchBar.value)
            uiState.value.exerciseListCatalog.value = newExerciseList

         } catch (e: Exception) {
            uiState.value.error.value = true
            uiState.value.loading.value = false
         }
      }
   }

   private fun getDayName(index: Int): String {
      return listOf(
         "Monday",
         "Tuesday",
         "Wednesday",
         "Thursday",
         "Friday",
         "Saturday",
         "Sunday"
      )[index % 7]
   }

   fun updateCurrentExercise(exercise: ExerciseItem) {
      uiState.value.currentExercise.value = exercise
   }

   fun deleteExerciseFromWorkout(workoutId: String, exerciseId: String) {
      _uiState.update { currentState ->
         val updatedWorkouts = currentState.workouts.map { workout ->
            if (workout.id == workoutId) {
               workout.copy(exercises = workout.exercises.filter { it.id != exerciseId })
            } else {
               workout
            }
         }
         currentState.copy(workouts = updatedWorkouts)
      }
   }

   private fun saveUser(user: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
      viewModelScope.launch {
         try {
            val userRef = db.collection("users").document(user.userId)
            userRef.set(user).await()
            onSuccess()
         } catch (e: Exception) {
            onError("Failed to save user data: ${e.message}")
         }
      }
   }


   fun signInWithEmail(
      email: String,
      password: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
   ) {
      viewModelScope.launch {
         try {
            auth.signInWithEmailAndPassword(email, password).await()
            val uid =
               auth.currentUser?.uid ?: throw IllegalStateException("User ID is null after sign in")
            val user = User(
               firstName = "Steve", // You might want to fetch this from Firestore instead
               lastName = "Levit",  // You might want to fetch this from Firestore instead
               email = email,
               userId = uid,
            )
            saveUser(user, onSuccess, onError)
         } catch (e: Exception) {
            onError("Sign In Failed: ${e.message}")
         }
      }
   }

   fun registerWithEmail(
      email: String,
      password: String,
      firstName: String,
      lastName: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
   ) {
      viewModelScope.launch {
         try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val uid = auth.currentUser?.uid
               ?: throw IllegalStateException("User ID is null after registration")
            val user = User(firstName, lastName, email, uid)
            saveUser(user, onSuccess, onError)
         } catch (e: Exception) {
            onError("Registration Failed: ${e.message}")
         }
      }
   }
}