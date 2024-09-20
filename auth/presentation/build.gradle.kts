plugins {
   alias(libs.plugins.android.library)
   alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
   compileSdkVersion = "android-34"
   namespace = "com.example.auth.presentation"
   compileSdkVersion = "android-34"

}

dependencies {

   implementation(libs.androidx.core.ktx)
   implementation(libs.material)
   implementation(libs.material)
   implementation(libs.androidx.ui.tooling.preview.android)
   testImplementation(libs.junit)
   androidTestImplementation(libs.androidx.junit)
   androidTestImplementation(libs.androidx.espresso.core)
}