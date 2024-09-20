plugins {
   alias(libs.plugins.android.library)
   alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
   compileSdkVersion = "android-34"

   namespace = "com.example.core.presentation.designsystem"


}

dependencies {

   implementation(libs.androidx.core.ktx)
   implementation(libs.androidx.compose.ui)
   implementation(libs.androidx.compose.ui.graphics)
   implementation(libs.androidx.compose.ui.tooling.preview)
   debugImplementation(libs.androidx.compose.ui.tooling)
   api(libs.androidx.compose.material3)

}