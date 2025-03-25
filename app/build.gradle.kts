import java.time.LocalDate

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    //alias(libs.plugins.firebase.crashlytics)

    id("com.google.devtools.ksp")
}

fun generateVersion(): Pair<Int, String> {
    val major = 2  // Используем как buildNumber и первую цифру версии
    val minor = 3  // Новые фичи
    val patch = 10  // Фиксы

    val date = LocalDate.now()
    val year = date.year % 100
    val month = date.monthValue.toString().padStart(2, '0').toInt()
    val day = date.dayOfMonth.toString().padStart(2, '0').toInt()

    val versionCode = year * 1000000 + month * 10000 + day * 100 + major
    val versionName = "$major.$minor.$patch"

    return Pair(versionCode, versionName)
}

android {
    namespace = "com.knopka.kz"
    compileSdk = 35

    val version = generateVersion()

    defaultConfig {
        applicationId = "com.filmhd1080.app"
        minSdk = 21
        targetSdk = 35

        versionCode = version.first
        versionName = version.second

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    //brand57 brand57

    signingConfigs {
        create("config") {
            keyAlias = "release"
            keyPassword = "release"
            storeFile = file("keystore/keystore.fhd1080apk.jks")
            storePassword = "release"
        }
    }

    buildTypes {

        getByName("debug") {
            //applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("config")
        }

        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("config")

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
    buildFeatures {
        compose = true
        buildConfig = true
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


    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.core.ktx)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")
    implementation(project(":wvcore"))
    implementation(project(":wvrss"))

    // SwipeRefreshLayout
    implementation(libs.androidx.swiperefreshlayout)

    //implementation(project(":features:webview"))
    implementation(libs.transport.api)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor2)
    implementation(libs.coil.network.ktor3)

    implementation(libs.firebase.messaging)
    //implementation(libs.onesignal)
    implementation(libs.onesignal)
    implementation("com.onesignal:OneSignal:[5.0.0, 5.99.99]")

//    implementation(libs.google.firebase.crashlytics)
//    implementation(libs.firebase.crashlytics)
    
    //implementation("io.appmetrica.analytics:analytics:7.2.0")
    implementation("io.appmetrica.analytics:analytics:7.7.0")
    implementation("io.appmetrica.analytics:push:4.0.0")

    implementation(libs.okhttp)
} 