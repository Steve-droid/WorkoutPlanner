package com.example.workoutplanner.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutplanner.ExerciseItem
import com.example.workoutplanner.TrainingCycle
import com.example.workoutplanner.User
import com.example.workoutplanner.Workout
import com.example.workoutplanner.WorkoutState
import com.example.workoutplanner.exerciseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class UIState(
   var workoutList: MutableState<List<WorkoutState>> = mutableStateOf(listOf(WorkoutState())),
   var exerciseListCatalog: MutableState<List<ExerciseItem>> = mutableStateOf(listOf(ExerciseItem())),
   var newWorkoutDay: MutableState<String> = mutableStateOf(""),
   var currentWorkout: MutableState<WorkoutState> = mutableStateOf(WorkoutState()),
   var newWorkout: MutableState<Workout> = mutableStateOf(Workout()),
   var currentExercise: MutableState<ExerciseItem> = mutableStateOf(ExerciseItem()),
   var newExercise: MutableState<ExerciseItem> = mutableStateOf(ExerciseItem()),
   var userId: MutableState<String> = mutableStateOf(""),
   var userName: MutableState<String> = mutableStateOf(""),
   var menuExpanded: MutableState<Boolean> = mutableStateOf(false),
   var exerciseSelected: MutableState<Boolean> = mutableStateOf(false),
   var loading: MutableState<Boolean> = mutableStateOf(false),
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

class SharedViewModel(context: Context) : ViewModel() {
   private var state = mutableStateOf(UIState())
   var uiState: MutableState<UIState> = state
   private val userCollectionRef = Firebase.firestore.collection("users")
   private val auth = FirebaseAuth.getInstance()
   val currentUser = auth.currentUser

   init {
      uiState.value.workoutList.value =
         mutableListOf(
            WorkoutState(day = "Sunday", workoutID = 0),
            WorkoutState(day = "Monday", workoutID = 1),
            WorkoutState(day = "Tuesday", workoutID = 2),
            WorkoutState(day = "Wednesday", workoutID = 3),
            WorkoutState(day = "Thursday", workoutID = 4),
            WorkoutState(day = "Friday", workoutID = 5),
            WorkoutState(day = "Saturday", workoutID = 6)
         )

      uiState.value.currentWorkout.value = uiState.value.workoutList.value[0]
      uiState.value.newWorkoutDay = mutableStateOf(uiState.value.currentWorkout.value.day)
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

   fun onClickAddExercise(
      onNavigateToCatalog: () -> Unit,
   ) {
      onNavigateToCatalog()
   }

   fun onSelectExercise() {
      val workoutToAddTo = uiState.value.workoutList.value.find {
         it.day == uiState.value.newWorkoutDay.value
      }

      workoutToAddTo?.exerciseState!!.value += uiState.value.newExercise.value
      workoutToAddTo!!.exercises += uiState.value.newExercise.value
   }

   fun removeExercise(exercise: ExerciseItem, workout: WorkoutState) {
      workout.exerciseState.value -= exercise
   }

   fun updateWorkoutDay(workout: Workout, newDay: String) {

      workout.day = newDay
   }

   fun signInWithEmail(
      email: String, password: String,
      onResult: (Boolean, String?) -> Unit,
      context: Context
   ) {
      val auth = FirebaseAuth.getInstance()
      auth.signInWithEmailAndPassword(email, password)
         .addOnCompleteListener { task ->
            if (task.isSuccessful) {
               val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
               val user = User(
                  firstName = "Steve",
                  lastName = "Levit",
                  email = email,
                  userId = uid,
               )
               saveUser(
                  user = user,
                  context = context
               ) // You may update the user's last login or similar
               onResult(true, null) // Login successful
            } else {
               onResult(false, task.exception?.message) // Login failed
            }
         }
   }

   fun registerWithEmail(
      email: String,
      password: String,
      firstName: String,
      lastName: String,
      onResult: (Boolean, String?) -> Unit
   ) {
      val auth = FirebaseAuth.getInstance()
      val db = FirebaseFirestore.getInstance()


      auth.createUserWithEmailAndPassword(email, password)
         .addOnCompleteListener { task ->
            if (task.isSuccessful) {
               val uid = auth.currentUser?.uid ?: ""
               db.collection("users").add(firstName)
               onResult(true, null) // Registration successful


            } else {
               onResult(false, task.exception?.message) // Registration failed
            }
         }
   }

   private fun saveUser(context: Context, user: User) = CoroutineScope(Dispatchers.IO).launch {
      try {
         userCollectionRef.add(user).await()
         withContext(Dispatchers.Main) {
            Toast.makeText(context, "Logged In!", Toast.LENGTH_LONG).show()
         }

      } catch (e: Exception) {
         withContext(Dispatchers.Main) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
         }
      }
   }

   fun onClickSaveCycle(context: Context, onReturn: () -> Unit) {
      val uid = auth.currentUser?.uid ?: return
      saveTrainingCycleToFirestore(uid, context, onReturn)


   }


   private fun saveTrainingCycleToFirestore(uid: String, context: Context, onReturn: () -> Unit) {
      val db = FirebaseFirestore.getInstance()

      val cycle = TrainingCycle(
         cycleName = "Hypertrophy",
         cycleDescription = "A 1-week hypertrophy program",
         numberOfWeeks = 1,
         workoutList = listOf(
            Workout(
               workoutID = uiState.value.workoutList.value[0].workoutID,
               name = uiState.value.workoutList.value[0].name,
               day = uiState.value.workoutList.value[0].day,
               exercises = uiState.value.workoutList.value[0].exercises
            ),
            Workout(
               workoutID = uiState.value.workoutList.value[1].workoutID,
               name = uiState.value.workoutList.value[1].name,
               day = uiState.value.workoutList.value[1].day,
               exercises = uiState.value.workoutList.value[1].exercises
            ),
            Workout(
               workoutID = uiState.value.workoutList.value[2].workoutID,
               name = uiState.value.workoutList.value[2].name,
               day = uiState.value.workoutList.value[2].day,
               exercises = uiState.value.workoutList.value[2].exercises
            ),
            Workout(
               workoutID = uiState.value.workoutList.value[3].workoutID,
               name = uiState.value.workoutList.value[3].name,
               day = uiState.value.workoutList.value[3].day,
               exercises = uiState.value.workoutList.value[3].exercises
            ),
            Workout(
               workoutID = uiState.value.workoutList.value[4].workoutID,
               name = uiState.value.workoutList.value[4].name,
               day = uiState.value.workoutList.value[4].day,
               exercises = uiState.value.workoutList.value[4].exercises
            ),
            Workout(
               workoutID = uiState.value.workoutList.value[5].workoutID,
               name = uiState.value.workoutList.value[5].name,
               day = uiState.value.workoutList.value[5].day,
               exercises = uiState.value.workoutList.value[5].exercises
            ),
            Workout(
               workoutID = uiState.value.workoutList.value[6].workoutID,
               name = uiState.value.workoutList.value[6].name,
               day = uiState.value.workoutList.value[6].day,
               exercises = uiState.value.workoutList.value[6].exercises
            )
         )
      )




      db.collection("users")
         .document("cycle for steve")
         .collection("trainingCycles")
         .add(cycle)
         .addOnSuccessListener {
            Toast.makeText(context, "Training Cycle Is Saved! Good Luck!", Toast.LENGTH_LONG).show()
            onReturn()
         }
         .addOnFailureListener { exception ->
            Toast.makeText(
               context,
               "Failed To Save Training Cycle. Reason: ${exception.message} ",
               Toast.LENGTH_LONG
            ).show()
         }
   }


}