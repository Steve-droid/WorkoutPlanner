package com.example.workoutplanner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workoutplanner.ui.theme.FadeAnimation
import com.example.workoutplanner.ui.theme.ScaleAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
   userName: String,
   onNavigateToCreateCycle: () -> Unit,
   onNavigateToViewCycles: () -> Unit
) {
   var visible by remember { mutableStateOf(false) }

   LaunchedEffect(Unit) {
      visible = true
   }

   Scaffold(
      topBar = {
         TopAppBar(
            title = { Text("Home") },
            colors = TopAppBarDefaults.topAppBarColors(
               containerColor = MaterialTheme.colorScheme.primaryContainer,
               titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
         )
      }
   ) { innerPadding ->
      FadeAnimation(visible = visible) {
         Column(
            modifier = Modifier
               .fillMaxSize()
               .padding(innerPadding)
               .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
         ) {
            WelcomeCard(userName)
            ScaleAnimation(visible = visible) {
               ActionButton(
                  text = "Create New Training Cycle",
                  icon = Icons.Default.Add,
                  onClick = onNavigateToCreateCycle,
                  containerColor = MaterialTheme.colorScheme.primary,
                  contentColor = MaterialTheme.colorScheme.onPrimary
               )
            }
            ScaleAnimation(visible = visible) {
               ActionButton(
                  text = "View Current Training Cycles",
                  icon = Icons.AutoMirrored.Filled.List,
                  onClick = onNavigateToViewCycles,
                  containerColor = MaterialTheme.colorScheme.secondary,
                  contentColor = MaterialTheme.colorScheme.onSecondary
               )
            }
         }
      }
   }
}

@Composable
fun WelcomeCard(userName: String) {
   Card(
      modifier = Modifier
         .fillMaxWidth()
         .padding(vertical = 16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
   ) {
      Column(
         modifier = Modifier.padding(16.dp),
         horizontalAlignment = Alignment.CenterHorizontally
      ) {
         Text(
            text = "Welcome back,",
            style = MaterialTheme.typography.headlineSmall
         )
         Text(
            text = userName,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
         )
      }
   }
}

@Composable
fun ActionButton(
   text: String,
   icon: ImageVector,
   onClick: () -> Unit,
   containerColor: Color,
   contentColor: Color
) {
   Button(
      onClick = onClick,
      modifier = Modifier
         .fillMaxWidth()
         .height(56.dp)
         .clip(RoundedCornerShape(8.dp)),
      colors = ButtonDefaults.buttonColors(
         containerColor = containerColor,
         contentColor = contentColor
      )
   ) {
      Row(
         horizontalArrangement = Arrangement.spacedBy(8.dp),
         verticalAlignment = Alignment.CenterVertically
      ) {
         Icon(icon, contentDescription = null)
         Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
         )
      }
   }
}