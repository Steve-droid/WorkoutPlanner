import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.workoutplanner.SharedViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
   viewModel: SharedViewModel,
   onNavigateToCreateCycle: () -> Unit,
   onNavigateToViewCycles: () -> Unit
) {
   val userFirstName by viewModel.userFirstName.collectAsState()

   LaunchedEffect(Unit) {
      viewModel.fetchUserData()
   }

   Scaffold(
      topBar = {
         TopAppBar(
            title = {
               Text(
                  "Workout Planner",
                  modifier = Modifier.fillMaxWidth(),
                  textAlign = TextAlign.Center
               )
            },
            colors = TopAppBarDefaults.topAppBarColors(
               containerColor = MaterialTheme.colorScheme.background
            )
         )
      }
   ) { innerPadding ->
      Column(
         modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(24.dp),
         verticalArrangement = Arrangement.spacedBy(24.dp),
         horizontalAlignment = Alignment.CenterHorizontally
      ) {
         WelcomeCard(userName = userFirstName ?: "User")
         ActionButton(
            text = "Create New Training Cycle",
            icon = Icons.Default.Add,
            onClick = onNavigateToCreateCycle,
            gradient = Brush.verticalGradient(
               listOf(
                  MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                  MaterialTheme.colorScheme.primary
               )
            )
         )
         ActionButton(
            text = "View Training Cycles",
            icon = Icons.AutoMirrored.Filled.List,
            onClick = onNavigateToViewCycles,
            gradient = Brush.verticalGradient(
               listOf(
                  MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                  MaterialTheme.colorScheme.secondary
               )
            )
         )
      }
   }
}

@Composable
fun WelcomeCard(userName: String) {
   Column(
      modifier = Modifier.padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      Text(
         text = "Welcome back,",
         style = MaterialTheme.typography.headlineSmall,
         color = MaterialTheme.colorScheme.primary
      )
      Text(
         text = userName,
         style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
         color = MaterialTheme.colorScheme.secondary
      )
   }
}

@Composable
fun ActionButton(
   text: String,
   icon: ImageVector,
   onClick: () -> Unit,
   gradient: Brush
) {
   Button(
      onClick = onClick,
      modifier = Modifier
         .fillMaxWidth()
         .height(56.dp),
      colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
      contentPadding = PaddingValues(0.dp)
   ) {
      Box(
         modifier = Modifier
            .fillMaxSize()
            .background(gradient),
         contentAlignment = Alignment.Center
      ) {
         Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
         ) {
            Icon(icon, contentDescription = null, tint = Color.White)
            Text(text, color = Color.White)
         }
      }
   }
}