plugins {
   alias(libs.plugins.android.library)
   alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
   namespace = "com.steve.auth.data"
   compileSdk = 34
   compileSdkVersion = "android-34"

   kotlinOptions {
      jvmTarget = "17"
   }

}

dependencies {
   implementation(projects.auth.domain)
   implementation(projects.core.domain)
   implementation(projects.core.data)
   implementation(libs.androidx.core.ktx)
}