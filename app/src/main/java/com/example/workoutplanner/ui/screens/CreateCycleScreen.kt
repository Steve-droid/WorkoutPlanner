package com.example.workoutplanner.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutplanner.model.ExerciseItem
import com.example.workoutplanner.model.Workout
import com.example.workoutplanner.viewmodel.CreateCycleViewModel

@Composable
fun CreateCycleScreen(
   cycleViewModel: CreateCycleViewModel= viewModel(),
   onNavigateToHome: () -> Unit,
   onNavigateToCatalog: (String) -> Unit,
   workoutDay:String = ""
) {
   var expanded by remember { mutableStateOf(false) }
   var changed by remember { mutableStateOf(false) }
   var cycleState : State<CreateCycleViewModel> = remember{mutableStateOf(cycleViewModel)}
   val workoutList by remember {
      cycleViewModel.workoutDays
   }



   LazyColumn(
      modifier = Modifier
         .fillMaxSize()
         .padding(16.dp)
         .padding(top = 20.dp)
         .background(MaterialTheme.colorScheme.background),
      verticalArrangement = Arrangement.SpaceEvenly
   ){


      items(workoutList) { workout: Workout ->

         var workoutState = remember {
            mutableStateOf(workout)
         }
         Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
               modifier = Modifier
                  .border(
                     width = 1.5.dp,
                     color = MaterialTheme.colorScheme.primary, // Border color
                     shape = MaterialTheme.shapes.small // Shape of the border
                  )
                  .padding(horizontal = 7.dp, vertical = 2.dp)

            ) {
               Text(
                  workout.day,
                  style = MaterialTheme.typography.titleMedium.copy(
                     color = MaterialTheme.colorScheme.onSurface
                  )
               )
            }

            IconButton(onClick = { expanded = true }, modifier = Modifier.size(22.dp)) {
               Icon(
                  Icons.Default.ArrowDropDown,
                  contentDescription = workout.day,
                  tint = MaterialTheme.colorScheme.onSurface
               )
            }
            DropdownMenu(
               expanded = expanded, onDismissRequest = { expanded = false }) {
               val dayOptions = listOf(
                  "Monday",
                  "Tuesday",
                  "Wednesday",
                  "Thursday",
                  "Friday",
                  "Saturday",
                  "Sunday"
               )
               dayOptions.forEach { day ->
                  DropdownMenuItem(
                     text = { Text(day, color = MaterialTheme.colorScheme.onSurface) },
                     onClick = {
                        cycleViewModel.updateWorkoutDay(workout, day)
                        expanded = false
                     }
                  )
               }
            }




         }

         workoutState.value.exercises.value.forEach(
            action = {
               ShowExerciseItem(
                  exercise = it,
                  onRemoveExercise = {exercise->
                     workout.exercises.value -= exercise
                  }
               )
            }
         )

         Button(
            onClick = {
               changed = true
               cycleViewModel.selectedDay = workoutDay
               onNavigateToCatalog(workoutDay)
               //cycleState.value.addExercise(workout = workoutState.value, newExercise)
               //changed = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
               containerColor = MaterialTheme.colorScheme.primary,
               contentColor = MaterialTheme.colorScheme.onPrimary
            )
         ) {
            Text(text = "+ Add an Exercise")
         }

      }
   }
}



@Composable
fun ShowExerciseItem(
   exercise: ExerciseItem,
   onRemoveExercise: (ExerciseItem) -> Unit
) {
   var exerciseState = remember{ mutableStateOf(exercise)}

   Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
   ) {
      Text(
         text = exerciseState.value.name,
         modifier = Modifier
            .weight(1f)
            .padding(end = 8.dp)
            .border(
               width = 1.dp,
               color = MaterialTheme.colorScheme.onSecondaryContainer,
               shape = MaterialTheme.shapes.small
            )
            .padding(12.dp)
      )

      IconButton(
         onClick ={ onRemoveExercise(exerciseState.value)},
         modifier = Modifier.size(24.dp)
      ) {
         Icon(
            Icons.Default.Delete,
            contentDescription = "Remove exercise",
            tint = MaterialTheme.colorScheme.error
         )
      }
   }
}

@Composable
fun ExerciseCatalog(
   workoutDay: String = "sunday",
   createCycleViewModel: CreateCycleViewModel = viewModel(),
   onNavigateToNewCycleScreen: () -> Unit,

) {
   var exerciseName by remember { mutableStateOf("") }
   val focusManager = LocalFocusManager.current

   var exerciseSelected by remember { mutableStateOf(false) }

   Box(modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)  // Apply a white background
      .clickable {
         focusManager.clearFocus()// Clear focus on click
         exerciseSelected = true
      }
   ) {


      Column(
         modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 25.dp)
            .clickable { focusManager.clearFocus() }  // Clear focus on click
      ) {
         TextField(
            value = exerciseName,
            onValueChange = {
               exerciseName = it
               createCycleViewModel.fetchExercises(exerciseName)
            },
            singleLine = true,
            label = { Text(text = "Search For Exercises", textAlign = TextAlign.Center) },
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 16.dp)
               .padding(bottom = 16.dp)
               .clip(RoundedCornerShape(12.dp))  // Apply rounded corners
               .background(MaterialTheme.colorScheme.onSurface),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
         )

         LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
         ) {
            items(createCycleViewModel.exerciseState.value.list){
                  exercise ->
               Exercise(
                  exercise = exercise,
                  onExerciseSelected = {exercise: ExerciseItem, day:String ->
                     onNavigateToNewCycleScreen()
                     createCycleViewModel.workoutDays.value.find { createCycleViewModel.selectedDay==day }?.let {
                        createCycleViewModel.addExerciseToWorkout(
                           userID = "1",
                           cycleID = "1",
                           workoutID = "1",
                           exercise = exercise,
                           workout = it
                        )
                     }
                  },
                  day = workoutDay)




            }


         }




      }

   }
}


// How each exercise looks like
@Composable
fun Exercise(
   exercise: ExerciseItem,
   day:String,
   onExerciseSelected: (ExerciseItem, String) -> Unit
){

   Card(
      shape = RoundedCornerShape(8.dp),
      elevation = CardDefaults.cardElevation(2.dp),
      modifier = Modifier
         .fillMaxWidth()
         .padding(vertical = 4.dp)
         .clip(RoundedCornerShape(8.dp))
         .background(MaterialTheme.colorScheme.surface)
         .clickable(onClick = { onExerciseSelected(exercise,day) })
   ) {

      Column(
         modifier = Modifier
            .padding(8.dp)
      ) {

         Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
               .background(
                  MaterialTheme.colorScheme.secondaryContainer,
                  shape = RoundedCornerShape(4.dp)
               ) // Light red background
               .padding(horizontal = 8.dp, vertical = 2.dp)
         ) {
            Text(
               text = exercise.muscle.uppercase(),
               style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = MaterialTheme.colorScheme.onSecondaryContainer
               ),
               fontSize = 12.sp
            )
         }

         Spacer(modifier = Modifier.height(4.dp))

         Text(
            text = exercise.name,
            style = MaterialTheme.typography.bodyLarge.copy(
               fontWeight = FontWeight.Medium,
               color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
               .fillMaxWidth()
               .border(
                  1.dp,
                  MaterialTheme.colorScheme.outline,
                  shape = RoundedCornerShape(4.dp)
               ) // Border around the exercise name
               .padding(8.dp)
         )
      }
   }
}


