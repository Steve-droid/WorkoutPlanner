import java.util.Properties


plugins {
   alias(libs.plugins.android.application)
   alias(libs.plugins.jetbrains.kotlin.android)
   alias(libs.plugins.kotlin.serialization)
}

android {
   namespace = "com.example.workoutplanner"
   compileSdk = 34

   defaultConfig {
      applicationId = "com.example.workoutplanner"
      minSdk = 34
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
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
   }
   kotlinOptions {
      jvmTarget = "1.8"
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
}

dependencies {


   //Compose ViewModel
   implementation(libs.androidx.lifecycle.viewmodel.compose)

   //Network calls
   implementation(libs.retrofit)

   //Json to Kotlin object mapping
   implementation(libs.converter.gson)

   //Image loading
   implementation(libs.coil.compose)

   implementation(libs.androidx.core.ktx)
   implementation(libs.androidx.lifecycle.runtime.ktx)
   implementation(libs.androidx.activity.compose)
   implementation(platform(libs.androidx.compose.bom.v20240900))
   implementation(libs.ui)
   implementation(libs.ui.graphics)
   implementation(libs.ui.tooling.preview)
   implementation(libs.material3)

   //Navigation
   implementation(libs.navigation.compose)
   implementation(libs.kotlinx.serialization.json)

   testImplementation(libs.junit)
   androidTestImplementation(libs.androidx.junit)
   androidTestImplementation(libs.androidx.espresso.core)
   androidTestImplementation(platform(libs.androidx.compose.bom.v20240900))
   androidTestImplementation(libs.ui.test.junit4)
   debugImplementation(libs.ui.tooling)
   debugImplementation(libs.ui.test.manifest)
}