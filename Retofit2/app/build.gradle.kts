plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.retofit2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.retofit2"
        minSdk = 24
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Network with Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // Load images with Glide
    implementation(libs.glide)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)



    implementation("com.google.android.material:material:1.11.0")

    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    implementation("com.tbuonomo:dotsindicator:4.2")

}