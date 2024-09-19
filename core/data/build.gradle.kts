plugins {
   alias(libs.plugins.android.library)
   alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
   namespace = "com.example.core.data"
   compileSdkVersion = "android-34"

}

dependencies {
   implementation(libs.timber)

   implementation(projects.core.domain)
   implementation(projects.core.database)
   implementation(libs.androidx.core.ktx)


}