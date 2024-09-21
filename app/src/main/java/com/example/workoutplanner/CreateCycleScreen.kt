package com.example.workoutplanner

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.workoutplanner.ui.theme.ScaleAnimation
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCycleScreen(
   vm: SharedViewModel,
   onNavigateToCatalog: (String) -> Unit,
   onNavigateToHome: () -> Unit
) {
   val uiState by vm.uiState.collectAsState()
   var showInitialDialog by remember { mutableStateOf(true) }
   var showSaveConfirmation by remember { mutableStateOf(false) }
   var showTooltip by remember { mutableStateOf(false) }
   var isLoading by remember { mutableStateOf(false) }
   val context = LocalContext.current

   LaunchedEffect(key1 = Unit) {
      delay(1000)
      showTooltip = true
   }

   if (showInitialDialog) {
      CycleDetailsDialog(
         onDismissRequest = { onNavigateToHome() },
         onConfirm = { name, days, weeks ->
            showInitialDialog = false
            vm.createTrainingCycle(
               cycleName = name,
               daysPerWeek = days,
               numberOfWeeks = weeks,
               onSuccess = {
                  Toast.makeText(context, "Training Cycle Created!", Toast.LENGTH_LONG).show()
               },
               onError = { errorMessage ->
                  Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
               }
            )
         }
      )
   }

   Scaffold { innerPadding ->

      LazyColumn(
         modifier = Modifier.padding(innerPadding)
      ) {

         item {
            Text(
               text = uiState.currentCycle.cycleName,
               style = MaterialTheme.typography.labelMedium,
            )
         }

         items(uiState.workouts, key = { it.id }) { workout ->
            WorkoutDayCard(
               vm = vm,
               workout = workout,
               onAddExercise = { onNavigateToCatalog(workout.id) }
            )
         }

         item {
            Button(
               onClick = { showSaveConfirmation = true },
               modifier = Modifier.fillMaxWidth(),
               enabled = uiState.workouts.any { it.exercises.isNotEmpty() }
            ) {
               Icon(Icons.Default.Done, contentDescription = null)
               Spacer(modifier = Modifier.width(8.dp))
               Text("Save Cycle")
            }

            AnimatedVisibility(
               visible = showTooltip,
               enter = fadeIn() + expandVertically(),
               exit = fadeOut() + shrinkVertically()
            ) {
               Surface(
                  modifier = Modifier
                     .padding(bottom = 8.dp),
                  shape = MaterialTheme.shapes.medium,
                  tonalElevation = 4.dp
               ) {
                  Row(
                     modifier = Modifier.padding(8.dp),
                     verticalAlignment = Alignment.CenterVertically
                  ) {
                     Icon(Icons.Default.Info, contentDescription = null)
                     Spacer(modifier = Modifier.width(8.dp))
                     Text("Click here to save your training cycle")
                  }
               }
            }
         }
      }


      // Loading overlay
      if (isLoading) {
         Box(
            modifier = Modifier
               .fillMaxSize()
               .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
         ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
         }
      }


      if (showSaveConfirmation) {
         AlertDialog(
            onDismissRequest = { showSaveConfirmation = false },
            title = { Text("Save Training Cycle") },
            text = { Text("Are you sure you want to save this training cycle?") },
            confirmButton = {
               Button(
                  onClick = {
                     showSaveConfirmation = false
                     isLoading = true
                     vm.saveCompletedTrainingCycle(
                        onSuccess = {
                           isLoading = false
                           Toast.makeText(
                              context,
                              "Training cycle saved successfully!",
                              Toast.LENGTH_LONG
                           ).show()
                           onNavigateToHome()
                        },
                        onError = { errorMessage ->
                           isLoading = false
                           Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                     )
                  }
               ) {
                  Text("Save")
               }
            },
            dismissButton = {
               TextButton(onClick = { showSaveConfirmation = false }) {
                  Text("Cancel")
               }
            }
         )
      }
   }
}

@Composable
fun WorkoutDayCard(
   workout: Workout,
   onAddExercise: () -> Unit,
   vm: SharedViewModel
) {


   Card(
      modifier = Modifier
         .fillMaxWidth()
         .padding(vertical = 4.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
   ) {
      Column(
         modifier = Modifier.padding(16.dp)
      ) {
         Text(
            text = "${workout.day} - ${workout.name}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
         )
         Spacer(modifier = Modifier.height(8.dp))

         workout.exercises.forEach { exercise ->
            ExerciseItemRow(exercise = exercise, workout = workout, vm = vm)
         }

         Button(
            onClick = onAddExercise,
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 8.dp)
         ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Exercise")
         }
      }
   }
}


@Composable
fun CycleDetailsDialog(
   onDismissRequest: () -> Unit,
   onConfirm: (String, Int, Int) -> Unit
) {
   var cycleName by remember { mutableStateOf("") }
   var daysPerWeek by remember { mutableStateOf("") }
   var numberOfWeeks by remember { mutableStateOf("") }

   Dialog(onDismissRequest = onDismissRequest) {
      Card(
         modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
         shape = RoundedCornerShape(16.dp)
      ) {
         Column(
            modifier = Modifier
               .fillMaxWidth()
               .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
         ) {
            Text(
               text = "Create New Training Cycle",
               style = MaterialTheme.typography.headlineSmall,
               fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
               value = cycleName,
               onValueChange = { cycleName = it },
               label = { Text("Cycle Name") },
               modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
               value = daysPerWeek,
               onValueChange = { daysPerWeek = it },
               label = { Text("Days per Week") },
               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
               modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
               value = numberOfWeeks,
               onValueChange = { numberOfWeeks = it },
               label = { Text("Number of Weeks") },
               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
               modifier = Modifier.fillMaxWidth()
            )

            Row(
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.End
            ) {
               TextButton(onClick = onDismissRequest) {
                  Text("Cancel")
               }
               Spacer(modifier = Modifier.width(8.dp))
               Button(
                  onClick = {
                     val days = daysPerWeek.toIntOrNull() ?: 0
                     val weeks = numberOfWeeks.toIntOrNull() ?: 0
                     if (cycleName.isNotBlank() && days > 0 && weeks > 0) {
                        onConfirm(cycleName, days, weeks)
                     }
                  }
               ) {
                  Text("Create")
               }
            }
         }
      }
   }
}

@Composable
fun ExerciseItemRow(
   exercise: ExerciseItem,
   workout: Workout,
   vm: SharedViewModel
) {
   Card(
      modifier = Modifier
         .fillMaxWidth()
         .padding(vertical = 4.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
   ) {
      Column(
         modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
      ) {
         Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
         ) {
            Text(
               text = exercise.name,
               style = MaterialTheme.typography.titleMedium,
               color = MaterialTheme.colorScheme.onPrimaryContainer,
               fontWeight = FontWeight.Bold
            )
            IconButton(
               onClick = {
                  vm.deleteExerciseFromWorkout(workoutId = workout.id, exerciseId = exercise.id)
               }
            ) {
               Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "Remove Exercise",
                  tint = MaterialTheme.colorScheme.error
               )
            }
         }
         Spacer(modifier = Modifier.height(4.dp))
         Text(
            text = exercise.muscle.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
         )
      }
   }
}

@Composable
fun ExerciseCatalog(
   vm: SharedViewModel,
   onNavigateToNewCycleScreen: () -> Unit,
   workoutId: String
) {


   LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(8.dp)

   ) {

      item {
         TextField(
            value = vm.getSearchBarQuery(),
            onValueChange = {
               vm.uiState.value.searchBar.value = it
               vm.fetchExercises()
            },
            label = {
               Text(
                  text = "Search For Exercises",
                  textAlign = TextAlign.Center,
                  style = MaterialTheme.typography.bodyMedium.copy(
                     color = MaterialTheme.colorScheme.onSurface
                  )

               )
            },
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 40.dp) // Add top padding to avoid overlap with the status bar
               .clip(RoundedCornerShape(12.dp)) // Add rounded corners
               .background(MaterialTheme.colorScheme.surface) // Background color
               .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline)), // Border color
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            singleLine = true, // Ensure single-line input for a cleaner look
            textStyle = TextStyle(
               fontSize = 16.sp // Adjust font size for better readability
            )
         )
      }

      if (vm.uiState.value.searchBar.value.isNotEmpty()) {

         items(vm.uiState.value.exerciseListCatalog.value) { exercise ->
            var visible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
               visible = true
            }

            ScaleAnimation(visible = visible) {

               vm.updateCurrentExercise(exercise)
               OutlinedCard(
                  shape = RoundedCornerShape(8.dp),
                  elevation = CardDefaults.cardElevation(2.dp),
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(vertical = 4.dp)
                     .clip(RoundedCornerShape(8.dp))
                     .background(MaterialTheme.colorScheme.surface)
                     .clickable {
                        vm.uiState.value.newExercise.value = exercise
                        vm.addExerciseToWorkout(exercise = exercise, workoutId = workoutId)
                        onNavigateToNewCycleScreen()
                     }
               ) {

                  Column(
                     modifier = Modifier.padding(8.dp)
                  ) {
                     // Muscle Group Label
                     Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                           .background(
                              MaterialTheme.colorScheme.secondaryContainer,  // Use theme secondary container color
                              shape = RoundedCornerShape(4.dp)
                           )
                           .padding(horizontal = 8.dp, vertical = 2.dp)
                     ) {
                        Text(
                           text = exercise.muscle.uppercase(),
                           style = MaterialTheme.typography.labelMedium.copy(
                              fontWeight = FontWeight.SemiBold,
                              color = MaterialTheme.colorScheme.onSecondaryContainer  // Use theme color
                           ),
                           fontSize = 12.sp
                        )
                     }

                     Spacer(modifier = Modifier.height(4.dp))

                     // Exercise Name with Border
                     Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                           fontWeight = FontWeight.Medium,
                           color = MaterialTheme.colorScheme.onSurface  // Use theme color
                        ),
                        modifier = Modifier
                           .fillMaxWidth()
                           .border(
                              1.dp,
                              MaterialTheme.colorScheme.outline,  // Use theme outline color
                              shape = RoundedCornerShape(4.dp)
                           )
                           .padding(8.dp)
                     )
                  }
               }
            }

         }
      }
   }
}