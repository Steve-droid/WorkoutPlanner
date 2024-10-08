import java.util.Properties


plugins {
   alias(libs.plugins.android.application)
   alias(libs.plugins.kotlin.serialization)
   alias(libs.plugins.org.jetbrains.kotlin.android)
   alias(libs.plugins.compose.compiler)
   alias(libs.plugins.google.gms.google.services)

}

android {
   namespace = "com.example.workoutplanner"
   compileSdkVersion = "android-35"

   defaultConfig {
      applicationId = "com.example.workoutplanner"
      minSdk = 28
      targetSdk = 34
      versionCode = 1
      versionName = "1.0"

      android.buildFeatures.buildConfig = true

      // Load the values from the .properties file
      val keystoreFile = project.rootProject.file("app/apikeys.properties")
      val properties = Properties()
      properties.load(keystoreFile.inputStream())

      //return empty key in case something goes wrong
      val apiKey = properties.getProperty("API_KEY") ?: ""

      buildConfigField(
         type = "String",
         name = "API_KEY",
         value = apiKey
      )

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
      vectorDrawables {
         useSupportLibrary = true
      }
   }

   buildTypes {
      release {
         isMinifyEnabled = false
         proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
         )
      }
   }
   compileOptions {
      sourceCompatibility = JavaVersion.VERSION_17
      targetCompatibility = JavaVersion.VERSION_17
   }

   buildFeatures {
      compose = true
   }
   composeOptions {
      kotlinCompilerExtensionVersion = "1.5.1"
   }
   packaging {
      resources {
         excludes += "/META-INF/{AL2.0,LGPL2.1}"
      }
   }
   kotlinOptions {
      jvmTarget = "17"
   }
}

dependencies {

   //Firebase
   // Import the BoM for the Firebase platform
   implementation(platform(libs.firebase.bom))

   // Add the dependency for the Realtime Database library
   // When using the BoM, you don't specify versions in Firebase library dependencies
   implementation(libs.firebase.database)
   //Compose ViewModel
   implementation(libs.androidx.lifecycle.viewmodel.compose)

   //Network calls
   implementation(libs.retrofit)

   //Json to Kotlin object mapping
   implementation(libs.converter.gson)
   implementation(libs.androidx.runtime)
   // Credentials+ navigation
   implementation(libs.androidx.compose.navigation)
   implementation(libs.kotlinx.serialization.json)
   implementation(libs.androidx.credentials.compat)
   implementation(libs.androidx.lifecycle.runtime.compose)

   implementation(libs.androidx.core.ktx)
   implementation(libs.androidx.activity.ktx)
   implementation(libs.androidx.foundation.android)
   implementation(libs.androidx.material3.android)
   implementation(libs.navigation.compose)
   implementation(libs.androidx.ui.tooling.preview.android)
   implementation(libs.firebase.firestore)
   implementation(libs.firebase.storage)
   implementation(libs.firebase.auth)

   testImplementation(libs.junit)
   androidTestImplementation(libs.androidx.junit)
   androidTestImplementation(libs.androidx.espresso.core)
}