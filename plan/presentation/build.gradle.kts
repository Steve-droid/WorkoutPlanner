plugins {
   alias(libs.plugins.android.library)
   alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
   namespace = "com.steve.plan.presentation"
   compileSdkVersion = "android-34"
}

dependencies {

   implementation(libs.androidx.core.ktx)
   implementation(libs.material)
   testImplementation(libs.junit)
   androidTestImplementation(libs.androidx.junit)
   androidTestImplementation(libs.androidx.espresso.core)
   implementation(projects.core.domain)
   implementation(projects.plan.domain)
}