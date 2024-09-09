import java.util.Properties


plugins {
   alias(libs.plugins.android.application)
   alias(libs.plugins.jetbrains.kotlin.android)
   alias(libs.plugins.kotlin.serialization)
   id("com.google.gms.google-services")
}

android {
   namespace = "com.example.workoutplanner"
   compileSdk = 35

   defaultConfig {
      applicationId = "com.example.workoutplanner"
      minSdk = 34
      targetSdk = 35
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

   implementation(libs.androidx.credentials)

   // optional - needed for credentials support from play services, for devices running
   // Android 13 and below.
   implementation(libs.androidx.credentials.play.services.auth)

   // Import the BoM for the Firebase platform
   implementation(platform(libs.firebase.bom))

   // Add the dependency for the Firebase Authentication library
   // When using the BoM, you don't specify versions in Firebase library dependencies
   implementation(libs.firebase.auth)

   // Also add the dependency for the Google Play services library and specify its version
   implementation(libs.play.services.auth)

   implementation(libs.firebase.analytics)

   implementation(platform(libs.firebase.bom))
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
   implementation(libs.firebase.database)
   implementation(libs.firebase.auth.ktx)

   testImplementation(libs.junit)
   androidTestImplementation(libs.androidx.junit)
   androidTestImplementation(libs.androidx.espresso.core)
   androidTestImplementation(platform(libs.androidx.compose.bom.v20240900))
   androidTestImplementation(libs.ui.test.junit4)
   debugImplementation(libs.ui.tooling)
   debugImplementation(libs.ui.test.manifest)
}