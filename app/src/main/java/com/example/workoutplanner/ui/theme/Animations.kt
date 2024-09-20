package com.example.workoutplanner.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable

@Composable
fun FadeAnimation(
   visible: Boolean,
   content: @Composable AnimatedVisibilityScope.() -> Unit
) {
   AnimatedVisibility(
      visible = visible,
      enter = fadeIn(animationSpec = tween(durationMillis = 300)),
      exit = fadeOut(animationSpec = tween(durationMillis = 300)),
      content = content
   )
}

@Composable
fun ScaleAnimation(
   visible: Boolean,
   content: @Composable AnimatedVisibilityScope.() -> Unit
) {
   AnimatedVisibility(
      visible = visible,
      enter = scaleIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
      exit = scaleOut(animationSpec = spring(stiffness = Spring.StiffnessLow)),
      content = content
   )
}