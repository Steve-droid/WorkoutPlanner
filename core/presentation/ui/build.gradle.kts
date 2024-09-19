plugins {
   alias(libs.plugins.android.library)
   alias(libs.plugins.compose.compiler)
   alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
   namespace = "com.example.core.presentation.ui"
   compileSdkVersion = "android-34"


}

dependencies {
   implementation(projects.auth.domain)
   implementation(projects.core.presentation.designsystem)
   implementation(libs.androidx.core.ktx)
   implementation(libs.androidx.ui.android)
   implementation(project(":core:domain"))

}