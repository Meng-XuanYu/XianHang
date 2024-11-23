plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.login"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.login"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("io.github.lapism:search:2.0.1")
    implementation ("com.google.android.flexbox:flexbox:3.0.0")
    implementation ("com.makeramen:roundedimageview:2.3.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(libs.constraintlayout)
    implementation(libs.glide)
    implementation(libs.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}