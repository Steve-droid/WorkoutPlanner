package com.example.workoutplanner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewCyclesScreen(
   viewModel: SharedViewModel,
   onNavigateBack: () -> Unit,
   onCycleSelected: (String) -> Unit
) {
   val cycles by viewModel.userCycles.collectAsState()
   var isLoading by remember { mutableStateOf(true) }
   var error by remember { mutableStateOf<String?>(null) }

   LaunchedEffect(key1 = Unit) {
      viewModel.fetchUserCycles(
         onSuccess = { isLoading = false },
         onError = { errorMessage ->
            isLoading = false
            error = errorMessage
         }
      )
   }

   Scaffold(
      topBar = {
         TopAppBar(
            title = { Text("My Training Cycles") },
            navigationIcon = {
               IconButton(onClick = onNavigateBack) {
                  Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Navigate back")
               }
            }
         )
      }
   ) { innerPadding ->
      when {
         isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
               CircularProgressIndicator()
            }
         }

         error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
               Text(text = "Error: $error")
            }
         }

         cycles.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
               Text(text = "No training cycles found.")
            }
         }

         else -> {
            LazyColumn(
               modifier = Modifier
                  .fillMaxSize()
                  .padding(innerPadding)
                  .padding(16.dp)
            ) {
               items(cycles) { cycle ->
                  CycleCard(
                     cycle,
                     onClick = { onCycleSelected(cycle.id) }
                  )
               }
            }
         }
      }
   }
}

@Composable
fun CycleCard(
   cycle: FirestoreTrainingCycle,
   onClick: () -> Unit
) {
   Card(
      modifier = Modifier
         .fillMaxWidth()
         .padding(vertical = 8.dp)
         .clickable(onClick = onClick),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
   ) {
      Column(
         modifier = Modifier.padding(16.dp)
      ) {
         Text(
            text = cycle.cycleName,
            style = MaterialTheme.typography.headlineSmall
         )
         Spacer(modifier = Modifier.height(8.dp))
         Text(
            text = "Number of Weeks: ${cycle.numberOfWeeks}",
            style = MaterialTheme.typography.bodyMedium
         )
      }
   }
}