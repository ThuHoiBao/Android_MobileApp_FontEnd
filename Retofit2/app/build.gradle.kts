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
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation ("com.google.android.gms:play-services-auth:21.3.0")
    implementation ("com.auth0.android:jwtdecode:2.0.0")
    implementation ("com.google.android.gms:play-services-auth:21.3.0")
    implementation ("com.google.android.gms:play-services-safetynet:18.1.0")

    // Thư viện Glide để tải và hiển thị hình ảnh
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.wdullaer:materialdatetimepicker:4.2.3")

}