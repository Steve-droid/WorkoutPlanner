package com.example.workoutplanner.view_exercise_catalog

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.workoutplanner.Exercise
import com.example.workoutplanner.MainViewModel


@Composable
fun ExerciseCatalog(navController: NavController,modifier: Modifier = Modifier){
   val workoutViewModel : MainViewModel = viewModel()
   val viewState by workoutViewModel.exerciseState
   Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceAround
   ) {
      Text(text = "This is the Exercise Catalog Screen",
         modifier = Modifier.fillMaxWidth(),
      )

      TextField(value = "", onValueChange = {
         workoutViewModel.fetchExercises(it)
      },
      )
   }

   Box(modifier = Modifier.fillMaxSize()){
      when{
        viewState.loading -> {
            CircularProgressIndicator(modifier.align(Alignment.Center))
         }

         viewState.error !=  null -> {
            Text(text = "Encountered an error")
         }

         // Loaded- Display Exercises
         else -> {
            ExerciseCatalog(exerciseList = viewState.list)
         }
      }
   }
}

@Composable
fun ExerciseCatalog(exerciseList: List<Exercise>){

   LazyColumn(
      modifier = Modifier
         .fillMaxSize()
         .background(Color.LightGray),
      contentPadding = PaddingValues(4.dp)
   ) {
      items(exerciseList){
            exercise ->
         ExerciseItem(exercise = exercise)
      }
   }
}

// How each exercise looks like
@Composable
fun ExerciseItem(exercise: Exercise){

   Card(
      shape = RoundedCornerShape(12.dp),
      elevation = CardDefaults.cardElevation(4.dp),
      modifier = Modifier
         .fillMaxWidth()
         .padding(8.dp)
   ) {
      Column(
         modifier = Modifier.padding(16.dp)
      ) {
         Text(
            text = "Muscle Group: ${exercise.muscle}".uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = Color.DarkGray
         )

         Spacer(modifier = Modifier.height(6.dp))
         Text(
            text = "Exercise Name: ${exercise.name}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
         )

         Spacer(modifier = Modifier.height(6.dp))
         Text(
            text = "Equipment: ${exercise.equipment}".uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = Color.DarkGray
         )

      }

   }
}





@Composable
fun QueryEditor( ExerciseEdit: ExerciseEdit , onQueryChanged: (String) -> Unit){
   var editedQuery by remember { mutableStateOf(ExerciseEdit.query) }
   var isEditing by remember { mutableStateOf(ExerciseEdit.isEditing) }

   Row(
      modifier = Modifier
         .fillMaxWidth()
         .background(Color.White)
         .padding(8.dp),
      horizontalArrangement = Arrangement.SpaceEvenly
   ) {
      Column {
         BasicTextField(
            value = editedQuery,
            onValueChange = { editedQuery = it },
            singleLine = true,
            modifier = Modifier
               .wrapContentSize()
               .padding(8.dp)
         )
      }
      Button(
         onClick = {
            isEditing = false
            onQueryChanged(editedQuery)
         }
      ) {
         Text("Save")
      }
   }

}



data class ExerciseEdit(var query: String = "", var isEditing: Boolean = false)

@Preview(showBackground = true)
@Composable
fun ExerciseCatalogPreview(){
   ExerciseCatalog(navController = rememberNavController())
}