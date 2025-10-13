plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")

    // 🔥 Tambahin plugin Google Services
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.aplikasi_rumah_sakit_rawat_jalan"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.aplikasi_rumah_sakit_rawat_jalan"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // 🔥 Tambahin Firebase BOM (versi sync otomatis)
    implementation(platform("com.google.firebase:firebase-bom:32.2.0"))

    // 🔥 Firestore
    implementation("com.google.firebase:firebase-firestore-ktx")

    // (Opsional) Auth, kalau mau login pakai Firebase
    implementation("com.google.firebase:firebase-auth-ktx")
}
