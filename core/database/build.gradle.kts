plugins {
   alias(libs.plugins.android.library)
   alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
   namespace = "com.example.core.database"
   compileSdkVersion = "android-34"


}

dependencies {
   implementation(libs.org.mongodb.bson)

   implementation(projects.core.domain)
   implementation(libs.androidx.core.ktx)
}