import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutplanner.ExerciseItem
import com.example.workoutplanner.ExerciseSet
import com.example.workoutplanner.SharedViewModel
import com.example.workoutplanner.Workout
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingCycleDetailScreen(
    viewModel: SharedViewModel,
    cycleId: String,
    onNavigateBack: () -> Unit
) {
    val cycle by viewModel.selectedCycle.collectAsState()
    val selectedWeek by viewModel.selectedWeek.collectAsState()
    val workouts by viewModel.workouts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = cycleId) {
        viewModel.loadTrainingCycle(cycleId)
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.uiEvents.collectLatest { event ->
            when (event) {
                is SharedViewModel.UiEvent.ShowSnackbar -> snackbarMessage = event.message
                SharedViewModel.UiEvent.ShowRemoveSetConfirmation -> TODO()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(cycle?.cycleName ?: "Training Cycle") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                actions = {
                    WeekSelector(
                        currentWeek = selectedWeek,
                        totalWeeks = cycle?.numberOfWeeks ?: 0,
                        onWeekSelected = { viewModel.selectWeek(it) }
                    )
                }
            )
        },
        snackbarHost = {
            snackbarMessage?.let { message ->
                Snackbar(
                    action = {
                        TextButton(onClick = { snackbarMessage = null }) {
                            Text("Dismiss")
                        }
                    }
                ) {
                    Text(message)
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
               .fillMaxSize()
               .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                       .fillMaxSize()
                       .padding(16.dp)
                ) {
                    items(workouts) { workout ->
                        WorkoutCard(
                            cycleId = cycleId,
                            workout = workout,
                            isFirstWeek = selectedWeek == 1,
                            viewModel = viewModel,
                            onRemoveSet = { workoutId, exerciseId, setIndex ->
                                viewModel.removeSet(cycleId, workoutId, exerciseId, setIndex)
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun WorkoutCard(
    cycleId: String,
    workout: Workout,
    isFirstWeek: Boolean,
    viewModel: SharedViewModel,
    onRemoveSet: (String, String, Int) -> Unit
) {
    Card(
        modifier = Modifier
           .fillMaxWidth()
           .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = workout.day,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            workout.exercises.forEach { exercise ->
                ExerciseItemShow(
                    cycleId = cycleId,
                    workoutId = workout.id,
                    exercise = exercise,
                    isFirstWeek = isFirstWeek,
                    viewModel = viewModel
                )
            }
        }
    }
}


@Composable
fun ExerciseItemShow(
    cycleId: String,
    workoutId: String,
    exercise: ExerciseItem,
    isFirstWeek: Boolean,
    viewModel: SharedViewModel
) {
    Column(
        modifier = Modifier
           .fillMaxWidth()
           .padding(vertical = 8.dp)
    ) {
        Text(
            text = exercise.name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        if (isFirstWeek) {
            exercise.sets.forEachIndexed { index, set ->
                SetRow(
                    cycleId = cycleId,
                    exerciseId = exercise.id,
                    workoutId = workoutId,
                    setIndex = index,
                    set = set,
                    viewModel = viewModel
                )

            }
            Button(
                onClick = { viewModel.addSet(cycleId, workoutId, exercise.id) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add Set")
            }
        } else {
            Text(
                text = "Sets and reps will be programmed after you complete the previous week of your training program.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}


@Composable
fun SetRow(
    cycleId: String,
    workoutId: String,
    exerciseId: String,
    setIndex: Int,
    set: ExerciseSet,
    viewModel: SharedViewModel
) {
    Row(
        modifier = Modifier
           .fillMaxWidth()
           .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Set ${setIndex + 1}:",
            modifier = Modifier.width(60.dp)
        )
        CenteredLargeNumericTextField(
            value = set.weight,
            onValueChange = { newWeight ->
                viewModel.updateWeight(cycleId, workoutId, exerciseId, setIndex, newWeight)
            },
            label = "Weight",
            suffix = "kg",
            modifier = Modifier
               .weight(1f)
               .padding(end = 8.dp)
        )
        CenteredLargeNumericTextField(
            value = set.reps,
            onValueChange = { newReps ->
                viewModel.updateReps(cycleId, workoutId, exerciseId, setIndex, newReps)
            },
            label = "Reps",
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = {
            viewModel.removeSet(cycleId, workoutId, exerciseId, setIndex)
        }) {
            Icon(Icons.Default.Delete, contentDescription = "Remove Set")
        }
    }
}

@Composable
fun WeekSelector(currentWeek: Int, totalWeeks: Int, onWeekSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Week $currentWeek",
                modifier = Modifier.padding(20.dp)
            )
            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Select week")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            (1..totalWeeks).forEach { week ->
                DropdownMenuItem(
                    text = { Text("Week $week") },
                    onClick = {
                        onWeekSelected(week)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CenteredLargeNumericTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    suffix: String = "",
    modifier: Modifier = Modifier
) {
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                value,
                TextRange(value.length)
            )
        )
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            if (newValue.text.all { it.isDigit() }) {
                textFieldValue = newValue
                onValueChange(newValue.text)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        ),
        trailingIcon = if (textFieldValue.text.isNotEmpty() && suffix.isNotEmpty()) {
            { Text(suffix, modifier = Modifier.padding(end = 8.dp)) }
        } else null,
        colors = TextFieldDefaults.colors(
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    )
}

