package com.example.workoutplanner.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutplanner.ExerciseItem
import com.example.workoutplanner.WorkoutState
import com.example.workoutplanner.viewmodel.SharedViewModel


@Composable
fun CreateCycleScreen(
   vm: SharedViewModel,
   onNavigateToCatalog: (String) -> Unit,
   onReturn: () -> Unit
) {
   val context = LocalContext.current

   LazyColumn(
      modifier = Modifier
         .fillMaxSize()
         .padding(
            top = 40.dp,
            start = 16.dp,
            end = 16.dp
         ), // Add padding to avoid overlap with status bar
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      items(vm.uiState.value.workoutList.value) { workout ->
         WorkoutDayCard(
            onNavigateToCatalog = onNavigateToCatalog,
            vm = vm,
            workout
         )
      }

      item {
         Button(onClick = {
            vm.onClickSaveCycle(context, onReturn)
         }) {
            Text(text = "Save Cycle")
         }
      }
   }
}


@Composable
fun WorkoutDayCard(
   onNavigateToCatalog: (String) -> Unit,
   vm: SharedViewModel,
   workout: WorkoutState
) {
   Card(
      modifier = Modifier
         .fillMaxWidth()
         .clip(RoundedCornerShape(8.dp))
         .border(BorderStroke(3.dp, MaterialTheme.colorScheme.primary)),
      elevation = CardDefaults.cardElevation(8.dp), // Add elevation to the card
      colors = CardDefaults.cardColors(
         containerColor = MaterialTheme.colorScheme.surface
      )
   ) {
      Column(
         modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
         verticalArrangement = Arrangement.Center,
         horizontalAlignment = Alignment.CenterHorizontally
      ) {
         Text(
            text = workout.day, // Assuming workoutDay has a 'day' property
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
         )

         Spacer(modifier = Modifier.height(8.dp))

         workout.exerciseState.value.forEach { exercise ->
            vm.uiState.value.currentExercise.value = exercise
            ExerciseItemRow(
               exercise = exercise,
               vm = vm,
               workout = workout
            )
            Spacer(modifier = Modifier.height(8.dp))
         }

         Button(
            onClick = {
               vm.uiState.value.newWorkoutDay.value = workout.day
               onNavigateToCatalog(workout.day)
            },
            modifier = Modifier
               .wrapContentWidth() // Makes the button only as wide as its content
               .padding(10.dp)
               .clip(RoundedCornerShape(5.dp)),
            colors = ButtonDefaults.buttonColors(
               containerColor = MaterialTheme.colorScheme.secondaryContainer,
               contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
         ) {
            Text(
               text = "+ Add an Exercise",
               style = MaterialTheme.typography.labelLarge,
               fontSize = 14.sp // Make the font slightly larger for emphasis
            )
         }
      }
   }
}


@Composable
fun ExerciseItemRow(
   exercise: ExerciseItem,
   workout: WorkoutState,
   vm: SharedViewModel
) {
   Card(
      modifier = Modifier
         .fillMaxWidth()
         .padding(vertical = 4.dp)
         .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline), RoundedCornerShape(8.dp)),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
   ) {

      Column(
         modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
      ) {


         Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
               .background(
                  MaterialTheme.colorScheme.secondaryContainer, // Light red background color
                  shape = RoundedCornerShape(4.dp)
               )
               .padding(horizontal = 8.dp, vertical = 2.dp)
         ) {
            Text(
               text = exercise.muscle.uppercase(),
               style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSecondaryContainer // Darker red text color
               ),
               fontSize = 12.sp // Slightly smaller font size
            )
         }

         Spacer(modifier = Modifier.height(4.dp))

         Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween, // Distribute space between components
            modifier = Modifier
               .fillMaxWidth()
               .padding(vertical = 4.dp)
         ) {

            Text(
               text = exercise.name,
               modifier = Modifier
                  .weight(1f) // Take up available horizontal space
                  .border(
                     BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                     RoundedCornerShape(4.dp)
                  )
                  .padding(12.dp) // Increase padding for a larger text area
                  .clip(RoundedCornerShape(4.dp)),
               style = MaterialTheme.typography.bodyLarge.copy(
                  fontSize = 16.sp,
                  color = MaterialTheme.colorScheme.onSurface
               ) // Increase font size
            )

            Spacer(modifier = Modifier.width(8.dp)) // Space between text and icon
            IconButton(
               onClick = { vm.removeExercise(exercise, workout) },
               modifier = Modifier.size(32.dp) // Make the icon button larger
            ) {
               Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "Remove Exercise",
                  tint = MaterialTheme.colorScheme.error,
                  modifier = Modifier.size(24.dp) // Adjust icon size within the button
               )
            }
         }
      }
   }
}

@Composable
fun ExerciseCatalog(
   vm: SharedViewModel,
   onNavigateToNewCycleScreen: () -> Unit
) {


   LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(8.dp)

   ) {

      item {
         TextField(
            value = vm.uiState.value.searchBar.value,
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
            vm.uiState.value.currentExercise.value = exercise

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
                     vm.onSelectExercise()
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