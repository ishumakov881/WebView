plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.walhalla.webview"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
//    kotlinOptions {
//        jvmTarget = "21"
//    }
}

dependencies {
    implementation(libs.core)
    implementation(libs.androidx.core.ktx)

//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.test.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
}
