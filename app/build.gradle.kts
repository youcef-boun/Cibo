plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    kotlin("plugin.serialization") version "2.0.0"

    id("com.google.dagger.hilt.android")
    id ("kotlin-kapt")

}

android {
    namespace = "com.youcef_bounaas.cibo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.youcef_bounaas.cibo"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.car.ui.lib)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


//Supabase
    implementation(platform("io.github.jan-tennert.supabase:bom:3.0.3"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.ktor:ktor-client-android:3.0.3")
    implementation("io.github.jan-tennert.supabase:storage-kt:3.0.3")







    //Auth
    implementation("io.github.jan-tennert.supabase:auth-kt:3.0.3")









    //Jetpack DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")


    implementation("androidx.activity:activity-ktx:1.7.2")  // For the Activity Result API
    // Optionally, if you still need Kotlin Extensions:
    implementation("androidx.activity:activity-ktx:1.7.2")






    //coil
    implementation("io.coil-kt:coil-compose:2.6.0")

  // Jetpack Compose
    implementation ("androidx.compose.runtime:runtime-livedata:1.6.0")

    //Dagger Hilt
    implementation ("com.google.dagger:hilt-android:2.54")
    kapt ("com.google.dagger:hilt-android-compiler:2.54")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")

   //extended Icons
    implementation("androidx.compose.material:material-icons-extended:1.7.6")

    //navigation
    implementation ("androidx.navigation:navigation-compose:2.8.5")


    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    implementation ("androidx.datastore:datastore-preferences:1.1.2")
    implementation ("androidx.datastore:datastore-core:1.1.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation ("androidx.compose.runtime:runtime-saveable:1.7.6")






}