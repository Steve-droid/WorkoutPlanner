package com.example.workoutplanner.ui.screens

import android.icu.text.ListFormatter.Width
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutplanner.viewmodel.CreateCycleViewModel

@Composable
fun CreateCycleScreen(
   vm: CreateCycleViewModel,
   onNavigateToHome: () -> Unit,
   onNavigateToCatalog: () -> Unit,
) {

      ShowWorkoutItem(vm = vm,onNavigateToCatalog = onNavigateToCatalog)

}

@Composable
fun ShowWorkoutItem(
   vm: CreateCycleViewModel,
   onNavigateToCatalog: () -> Unit
){
   LazyColumn(
      modifier = Modifier
         .border(
            border = BorderStroke(4.dp, MaterialTheme.colorScheme.errorContainer),
            RoundedCornerShape(3.dp)
         )
         .padding(20.dp),
      contentPadding = PaddingValues(60.dp),
      verticalArrangement = Arrangement.SpaceBetween,
      horizontalAlignment = Alignment.CenterHorizontally,
   ) {
      items(vm.uiState.value.workoutList.value) { it ->
         vm.uiState.value.currentWorkout.value = it
       Column(
           modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.surfaceDim)
        ) {


            Row(
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.Center,
               ) {

               Text(
                  text = it.day,
                  style = MaterialTheme.typography.titleSmall.copy(
                     color = MaterialTheme.colorScheme.onSurface
                  )
               )
               }

               it.exercises.value.forEach {
                  vm.uiState.value.currentExercise.value = it
                     ExerciseItemRow(onRemoveExercise = {
                        vm.removeExercise(it)
                     }, vm = vm)
                  }
               }

         Spacer(modifier = Modifier.padding(all = 20.dp))

               Text(
                  text = "Add an Exercise",
                  modifier = Modifier
                     .clickable {
                        vm.uiState.value.newWorkoutDay.value = it.day
                        vm.onClickAddExercise(onNavigateToCatalog = onNavigateToCatalog)
                     }
                     .clip(RoundedCornerShape(5.dp))
                     .background(MaterialTheme.colorScheme.surfaceVariant)
                     .padding(10.dp),
                  style = MaterialTheme.typography.labelLarge,
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
               )
       }
      }
   }




@Composable
fun ExerciseItemRow(
   vm: CreateCycleViewModel,
    onRemoveExercise: () -> Unit
) {
   val currentExercise = remember {
    vm.uiState.value.currentExercise.value
   }
    Row(
        verticalAlignment = Alignment.CenterVertically, // Align the text field and icon
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text= currentExercise.name,
            modifier = Modifier
               .weight(1f) // Take up available space
               .fillMaxSize()
               .border(
                  BorderStroke(2.dp, Color.Gray),
                  shape = RoundedCornerShape(2.dp)
               )
               .padding(6.dp)
               .clip(RoundedCornerShape(2.dp)),

        )
        IconButton(
            onClick = onRemoveExercise,
            modifier = Modifier.size(24.dp) // Adjust icon size if necessary
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove Exercise",
                tint = Color.Red
            )
        }
    }
}

@Composable
fun ShowExerciseItem(
   vm: CreateCycleViewModel,
) {
   Card(
      shape = RoundedCornerShape(8.dp),  // Slightly reduce the corner radius
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),  // Lower the elevation for a subtler shadow
      modifier = Modifier
         .fillMaxWidth()
         .padding(vertical = 4.dp)  // Reduce the vertical padding between items
         .clip(RoundedCornerShape(8.dp))
         .background(Color.White)
   ) {
      Column(
         modifier = Modifier
            .padding(8.dp)  // Reduce the inner padding for a more compact look
      ) {
         // Muscle Group Label
         Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
               .background(
                  Color(0xFFFFEBEE),
                  shape = RoundedCornerShape(4.dp)
               ) // Adjusted color for a softer red background
               .padding(horizontal = 8.dp, vertical = 2.dp)  // Reduced padding inside the label
         ) {
            Text(
               text =  vm.uiState.value.currentExercise.value.muscle.uppercase(),
               style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFC62828)  // Slightly darker red text color
               ),
               fontSize = 12.sp  // Slightly smaller font size for a more compact look
            )
         }

         Spacer(modifier = Modifier.height(4.dp))  // Reduced spacing between muscle group and exercise name

         // Exercise Name with Border
         Text(
            text = vm.uiState.value.currentExercise.value.name,
            style = MaterialTheme.typography.bodyLarge.copy(
               fontWeight = FontWeight.Medium,
               color = Color.Black
            ),
            modifier = Modifier
               .fillMaxWidth()
               .border(
                  1.dp,
                  Color.Gray,
                  shape = RoundedCornerShape(4.dp)
               ) // Border around the exercise name
               .padding(8.dp)
         )
      }
   }
}

@Composable
fun ExerciseCatalog(
   vm: CreateCycleViewModel,
   onNavigateToNewCycleScreen: () -> Unit ) {

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
            label = { Text(text = "Search For Exercises", textAlign = TextAlign.Center) },
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 16.dp)
               .padding(bottom = 16.dp)
               .clip(RoundedCornerShape(12.dp))
               .background(MaterialTheme.colorScheme.onPrimary),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
         )
      }

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
                  vm.uiState.value.newExercise.value.name = exercise.name
                  vm.uiState.value.newExercise.value.muscle = exercise.muscle
                  vm.onSelectExercise(onNavigateToNewCycleScreen)
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
















