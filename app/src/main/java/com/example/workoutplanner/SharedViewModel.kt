package com.example.workoutplanner

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class SharedViewModel : ViewModel() {
   private val _uiState = MutableStateFlow(UIState())
   private val _userCycles = MutableStateFlow<List<FirestoreTrainingCycle>>(emptyList())
   private val _selectedCycle = MutableStateFlow<FirestoreTrainingCycle?>(null)
   val selectedCycle: StateFlow<FirestoreTrainingCycle?> = _selectedCycle
   private val _selectedWeek = MutableStateFlow(1)
   val selectedWeek: StateFlow<Int> = _selectedWeek
   val userCycles: StateFlow<List<FirestoreTrainingCycle>> = _userCycles
   val uiState: StateFlow<UIState> = _uiState
   private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
   val workouts: StateFlow<List<Workout>> = _workouts
   private val auth = FirebaseAuth.getInstance()
   private val db = FirebaseFirestore.getInstance()
   private val _uiEvents = MutableSharedFlow<UiEvent>()
   val uiEvents = _uiEvents.asSharedFlow()
   private val _isLoading = MutableStateFlow(false)
   val isLoading: StateFlow<Boolean> = _isLoading
   private val _userFirstName = MutableStateFlow<String?>(null)
   val userFirstName: StateFlow<String?> = _userFirstName

   sealed class UiEvent {
      data class ShowSnackbar(val message: String) : UiEvent()
      object ShowRemoveSetConfirmation : UiEvent()
   }


   fun updateSearchBarQuery(newQuery: String) {
      uiState.value.searchBar.value = newQuery
   }

   fun updateWorkoutDay(workout: Workout, onNavigateToCatalog: (String) -> Unit) {
      uiState.value.newWorkoutDay.value = workout.day
      onNavigateToCatalog(workout.day)
   }

   fun updateCurrentWorkout(newWorkout: Workout) {
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
               Workout(
                  id = UUID.randomUUID().toString(),
                  day = getDayName(index),
                  name = "Workout ${index + 1}",
                  exercises = emptyList()
               )
            }

            val cycle = FirestoreTrainingCycle(
               id = UUID.randomUUID().toString(),
               cycleName = cycleName,
               numberOfWeeks = numberOfWeeks,
               workoutList = workouts
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
      workouts: List<Workout>
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


   fun signInWithEmail(
      email: String,
      password: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
   ) {
      viewModelScope.launch {
         try {
            auth.signInWithEmailAndPassword(email, password).await()
            val uid = auth.currentUser?.uid
               ?: throw IllegalStateException("User ID is null after sign in")

            // Fetch user data from Firestore
            val userDoc = db.collection("users").document(uid).get().await()
            val user = userDoc.toObject(User::class.java)

            if (user != null) {
               _userFirstName.value = user.firstName
               onSuccess()
            } else {
               onError("Failed to fetch user data")
            }
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
            _userFirstName.value = firstName
         } catch (e: Exception) {
            onError("Registration Failed: ${e.message}")
         }
      }
   }

   private fun saveUser(user: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
      viewModelScope.launch {
         try {
            val userRef = db.collection("users").document(user.userId)
            userRef.set(user).await()
            _userFirstName.value = user.firstName
            onSuccess()
         } catch (e: Exception) {
            onError("Failed to save user data: ${e.message}")
         }
      }
   }

   // Add a function to fetch user data when the app starts
   fun fetchUserData() {
      viewModelScope.launch {
         try {
            val uid = auth.currentUser?.uid ?: return@launch
            val userDoc = db.collection("users").document(uid).get().await()
            val user = userDoc.toObject(User::class.java)
            if (user != null) {
               _userFirstName.value = user.firstName
            }
         } catch (e: Exception) {
            Log.e("SharedViewModel", "Error fetching user data: ${e.message}")
         }
      }
   }

   fun fetchUserCycles(
      onSuccess: () -> Unit,
      onError: (String) -> Unit
   ) {
      viewModelScope.launch {
         try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
               ?: throw IllegalStateException("User not logged in")

            val cyclesCollection = FirebaseFirestore.getInstance()
               .collection("users")
               .document(uid)
               .collection("trainingCycles")

            val cycles =
               cyclesCollection
                  .get()
                  .await()
                  .toObjects(FirestoreTrainingCycle::class.java)

            _userCycles.value = cycles
            onSuccess()
         } catch (e: Exception) {
            onError("Error fetching cycles: ${e.message}")
         }
      }
   }


   fun loadTrainingCycle(cycleId: String) {
      if (cycleId.isBlank()) {
         Log.e("SharedViewModel", "Attempted to load cycle with empty ID")
         return
      }

      viewModelScope.launch {
         try {
            _isLoading.value = true
            val uid = FirebaseAuth.getInstance().currentUser?.uid
               ?: throw IllegalStateException("User not logged in")

            Log.d("SharedViewModel", "Loading cycle with ID: $cycleId for user: $uid")

            val cycleDoc = FirebaseFirestore.getInstance()
               .collection("users")
               .document(uid)
               .collection("trainingCycles")
               .document(cycleId)

            val cycle = cycleDoc.get().await().toObject(FirestoreTrainingCycle::class.java)
            Log.d("SharedViewModel", "Loaded cycle: $cycle")

            if (cycle != null) {
               _selectedCycle.value = cycle
               loadWorkouts(cycleDoc)
            } else {
               Log.e("SharedViewModel", "Failed to parse cycle data")
            }
         } catch (e: Exception) {
            Log.e("SharedViewModel", "Error loading cycle: ${e.message}", e)
         } finally {
            _isLoading.value = false
         }
      }
   }

   private suspend fun loadWorkouts(cycleDoc: DocumentReference) {
      try {
         val workoutsCollection = cycleDoc.collection("workouts")
         val workoutsDocs = workoutsCollection.get().await()
         val workoutsList = workoutsDocs.mapNotNull { doc ->
            doc.toObject(Workout::class.java).also { workout ->
               workout.id = doc.id
               loadExercises(doc.reference, workout)
            }
         }
         _workouts.value = workoutsList
         Log.d("SharedViewModel", "Loaded workouts: $workoutsList")
      } catch (e: Exception) {
         Log.e("SharedViewModel", "Error loading workouts: ${e.message}", e)
      }
   }

   private suspend fun loadExercises(workoutDoc: DocumentReference, workout: Workout) {
      try {
         val exercisesCollection = workoutDoc.collection("exercises")
         val exercisesDocs = exercisesCollection.get().await()
         workout.exercises = exercisesDocs.mapNotNull { doc ->
            doc.toObject(ExerciseItem::class.java).also { exercise ->
               exercise.id = doc.id
            }
         }
      } catch (e: Exception) {
         Log.e(
            "SharedViewModel",
            "Error loading exercises for workout ${workout.id}: ${e.message}",
            e
         )
      }
   }

   fun selectWeek(week: Int) {
      _selectedWeek.value = week
   }

   fun addSet(cycleId: String, workoutId: String, exerciseId: String) {
      viewModelScope.launch {
         try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
               ?: throw IllegalStateException("User not logged in")

            val exerciseRef = db.collection("users").document(uid)
               .collection("trainingCycles").document(cycleId)
               .collection("workouts").document(workoutId)
               .collection("exercises").document(exerciseId)

            // Get the current exercise
            val exercise = exerciseRef.get().await().toObject(ExerciseItem::class.java)
               ?: throw IllegalStateException("Exercise not found")

            // Add a new set
            val updatedSets = exercise.sets + ExerciseSet()

            // Update the exercise in Firestore
            exerciseRef.update("sets", updatedSets).await()

            // Update the local state
            _workouts.update { currentWorkouts ->
               currentWorkouts.map { workout ->
                  if (workout.id == workoutId) {
                     workout.copy(
                        exercises = workout.exercises.map { ex ->
                           if (ex.id == exerciseId) {
                              ex.copy(sets = updatedSets)
                           } else ex
                        }
                     )
                  } else workout
               }
            }

            Log.d("SharedViewModel", "Set added successfully")
         } catch (e: Exception) {
            Log.e("SharedViewModel", "Error adding set: ${e.message}", e)
         }
      }
   }


   fun removeSet(cycleId: String, workoutId: String, exerciseId: String, setIndex: Int) {
      viewModelScope.launch {
         try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
               ?: throw IllegalStateException("User not logged in")

            val exerciseRef = db.collection("users").document(uid)
               .collection("trainingCycles").document(cycleId)
               .collection("workouts").document(workoutId)
               .collection("exercises").document(exerciseId)

            // Get the current exercise
            val exercise = exerciseRef.get().await().toObject(ExerciseItem::class.java)
               ?: throw IllegalStateException("Exercise not found")

            // Prevent removing the last set
            if (exercise.sets.size <= 1) {
               _uiEvents.emit(UiEvent.ShowSnackbar("Cannot remove the last set"))
               return@launch
            }

            // Remove the set at the specified index
            val updatedSets = exercise.sets.filterIndexed { index, _ -> index != setIndex }

            // Update the exercise in Firestore
            exerciseRef.update("sets", updatedSets).await()

            // Update the local state
            _workouts.update { currentWorkouts ->
               currentWorkouts.map { workout ->
                  if (workout.id == workoutId) {
                     workout.copy(
                        exercises = workout.exercises.map { ex ->
                           if (ex.id == exerciseId) {
                              ex.copy(sets = updatedSets)
                           } else ex
                        }
                     )
                  } else workout
               }
            }

            _uiEvents.emit(UiEvent.ShowSnackbar("Set removed successfully"))
         } catch (e: Exception) {
            _uiEvents.emit(UiEvent.ShowSnackbar("Error removing set: ${e.message}"))
            Log.e("SharedViewModel", "Error removing set: ${e.message}", e)
         }
      }
   }

   fun updateWeight(
      cycleId: String,
      workoutId: String,
      exerciseId: String,
      setIndex: Int,
      weight: String
   ) {
      updateExerciseSet(cycleId, workoutId, exerciseId, setIndex) { it.copy(weight = weight) }
   }

   fun updateReps(
      cycleId: String,
      workoutId: String,
      exerciseId: String,
      setIndex: Int,
      reps: String
   ) {
      updateExerciseSet(cycleId, workoutId, exerciseId, setIndex) { it.copy(reps = reps) }
   }

   private fun updateExerciseSet(
      cycleId: String,
      workoutId: String,
      exerciseId: String,
      setIndex: Int,
      update: (ExerciseSet) -> ExerciseSet
   ) {
      viewModelScope.launch {
         try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
               ?: throw IllegalStateException("User not logged in")

            val exerciseRef = db.collection("users").document(uid)
               .collection("trainingCycles").document(cycleId)
               .collection("workouts").document(workoutId)
               .collection("exercises").document(exerciseId)

            // Get the current exercise
            val exercise = exerciseRef.get().await().toObject(ExerciseItem::class.java)
               ?: throw IllegalStateException("Exercise not found")

            // Update the specific set
            val updatedSets = exercise.sets.mapIndexed { index, set ->
               if (index == setIndex) update(set) else set
            }

            // Update the exercise in Firestore
            exerciseRef.update("sets", updatedSets).await()

            // Update the local state
            updateLocalExercise(cycleId, workoutId, exerciseId) { it.copy(sets = updatedSets) }

            Log.d("SharedViewModel", "Set updated successfully")
         } catch (e: Exception) {
            Log.e("SharedViewModel", "Error updating set: ${e.message}", e)
         }
      }
   }

   private fun updateLocalExercise(
      cycleId: String,
      workoutId: String,
      exerciseId: String,
      update: (ExerciseItem) -> ExerciseItem
   ) {
      _selectedCycle.update { cycle ->
         cycle?.copy(
            workoutList = cycle.workoutList.map { workout ->
               if (workout.id == workoutId) {
                  workout.copy(
                     exercises = workout.exercises.map { exercise ->
                        if (exercise.id == exerciseId) update(exercise) else exercise
                     }
                  )
               } else workout
            }
         )
      }
   }


}