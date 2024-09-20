import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Define custom colors
val md_theme_light_primary = Color(0xFF006C4C)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFF8AF8C7)
val md_theme_light_onPrimaryContainer = Color(0xFF002114)
val md_theme_light_secondary = Color(0xFF4C6358)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFCEE9DA)
val md_theme_light_onSecondaryContainer = Color(0xFF092017)

val md_theme_dark_primary = Color(0xFF6DDBAC)
val md_theme_dark_onPrimary = Color(0xFF003825)
val md_theme_dark_primaryContainer = Color(0xFF005138)
val md_theme_dark_onPrimaryContainer = Color(0xFF8AF8C7)
val md_theme_dark_secondary = Color(0xFFB3CCBE)
val md_theme_dark_onSecondary = Color(0xFF1F352B)
val md_theme_dark_secondaryContainer = Color(0xFF354B41)
val md_theme_dark_onSecondaryContainer = Color(0xFFCEE9DA)

// Create color schemes
private val LightColorScheme = lightColorScheme(
   primary = md_theme_light_primary,
   onPrimary = md_theme_light_onPrimary,
   primaryContainer = md_theme_light_primaryContainer,
   onPrimaryContainer = md_theme_light_onPrimaryContainer,
   secondary = md_theme_light_secondary,
   onSecondary = md_theme_light_onSecondary,
   secondaryContainer = md_theme_light_secondaryContainer,
   onSecondaryContainer = md_theme_light_onSecondaryContainer
)

private val DarkColorScheme = darkColorScheme(
   primary = md_theme_dark_primary,
   onPrimary = md_theme_dark_onPrimary,
   primaryContainer = md_theme_dark_primaryContainer,
   onPrimaryContainer = md_theme_dark_onPrimaryContainer,
   secondary = md_theme_dark_secondary,
   onSecondary = md_theme_dark_onSecondary,
   secondaryContainer = md_theme_dark_secondaryContainer,
   onSecondaryContainer = md_theme_dark_onSecondaryContainer
)

// Custom Typography
val AppTypography = Typography(
   headlineLarge = TextStyle(
      fontFamily = FontFamily.Default,
      fontWeight = FontWeight.Bold,
      fontSize = 32.sp,
      lineHeight = 40.sp,
      letterSpacing = 0.sp
   ),
   headlineMedium = TextStyle(
      fontFamily = FontFamily.Default,
      fontWeight = FontWeight.SemiBold,
      fontSize = 28.sp,
      lineHeight = 36.sp,
      letterSpacing = 0.sp
   ),
   titleLarge = TextStyle(
      fontFamily = FontFamily.Default,
      fontWeight = FontWeight.Normal,
      fontSize = 22.sp,
      lineHeight = 28.sp,
      letterSpacing = 0.sp
   ),
   bodyLarge = TextStyle(
      fontFamily = FontFamily.Default,
      fontWeight = FontWeight.Normal,
      fontSize = 16.sp,
      lineHeight = 24.sp,
      letterSpacing = 0.5.sp
   )
)

// Custom Shapes
val AppShapes = Shapes(
   small = RoundedCornerShape(4.dp),
   medium = RoundedCornerShape(8.dp),
   large = RoundedCornerShape(16.dp)
)

@Composable
fun WorkoutPlannerTheme(
   darkTheme: Boolean = isSystemInDarkTheme(),
   content: @Composable () -> Unit
) {
   val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

   MaterialTheme(
      colorScheme = colorScheme,
      typography = AppTypography,
      shapes = AppShapes,
      content = content
   )
}