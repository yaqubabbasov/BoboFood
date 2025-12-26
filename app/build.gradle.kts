@file:Suppress("DEPRECATION")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
  //  id("com.google.devtools.ksp")

}

android {
    namespace = "com.yaqubabbasov.bobofood"
    compileSdk = 36
    buildFeatures{
        viewBinding=true
        dataBinding=true

    }


    defaultConfig {
        applicationId = "com.yaqubabbasov.bobofood"
        minSdk = 27
        targetSdk = 36
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
}

dependencies {
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.6.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.5.0")
    //Hilt
    //implementation("com.google.dagger:hilt-android:2.57")
    implementation(libs.hilt.android)
    implementation(libs.firebase.storage)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    //ksp(libs.hilt.compiler)

    kapt("com.google.dagger:hilt-android-compiler:2.57")
    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //Lottie
    implementation("com.airbnb.android:lottie:6.1.0")
    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.2.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation(libs.firebase.auth)
    //Image Slider
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")

    //implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.0")

    //Room

   implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
     kapt(libs.androidx.room.compiler)
    // ksp("com.google.dagger:hilt-compiler:2.57")


    //room



    /*implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.lifecycle.runtime.ktx)*/
    //hilt




// Credentials + GoogleID
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.2.0")


    implementation("androidx.core:core-splashscreen:1.0.1")






    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}