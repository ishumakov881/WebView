import java.time.LocalDate

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.hilt)
//    alias(libs.plugins.google.services)
//    alias(libs.plugins.firebase.crashlytics)

    //id("com.google.devtools.ksp")
    alias(libs.plugins.ksp)
}

fun generateVersion(): Pair<Int, String> {
    val major = 2  // Используем как buildNumber и первую цифру версии
    val minor = 4  // Новые фичи
    val patch = 14  // Фиксы

    val date = LocalDate.now()
    val year = date.year % 100
    val month = date.monthValue.toString().padStart(2, '0').toInt()
    val day = date.dayOfMonth.toString().padStart(2, '0').toInt()

    val versionCode = year * 1000000 + month * 10000 + day * 100 + major
    val versionName = "$major.$minor.$patch"

    return Pair(versionCode, versionName)
}

android {

    lint {
        baseline = file("lint-baseline.xml")
    }

    //namespace = "com.knopka.kz"
    namespace = "com.knopka.kz"

    compileSdk = libs.versions.android.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.android.buildTools.get()

    val version = generateVersion()

    defaultConfig {
        //applicationId = "com.knopka.kz"
        applicationId = "com.example.app"

        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()

        versionCode = version.first
        versionName = version.second

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    //brand57 brand57

    signingConfigs {
        create("knopka") {
            keyAlias = "knopka"
            keyPassword = "knopka"
            storeFile = file("keystore/brand57.jks")
            storePassword = "brand57"
        }

        create("pozitiv") {
            keyAlias = "knopka"
            keyPassword = "knopka"
            storeFile = file("keystore/brand57.jks")
            storePassword = "brand57"
        }

    }

    buildTypes {

        getByName("debug"){
            applicationIdSuffix = ".debug"
            //signingConfig = signingConfigs.getByName("config")
        }

        getByName("release") {
            isMinifyEnabled = false
            //signingConfig = signingConfigs.getByName("config")

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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }



    flavorDimensions += "client"

    productFlavors {

        create("knopka") {
            dimension = "client"
            applicationId = "com.knopka.kz"
            versionNameSuffix = "-knopka"
            resValue("string", "app_name", "knopka.kz")
            signingConfig = signingConfigs.getByName("knopka")
        }

        create("pozitiv") {
            dimension = "client"
            applicationId = "com.appbery.appberypositiverf"
            versionNameSuffix = "-positiverf"
            resValue("string", "app_name", "Позитив")
            signingConfig = signingConfigs.getByName("pozitiv")
        }
    }
}

dependencies {
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.material3)
    implementation(platform("androidx.compose:compose-bom:2025.01.01"))
    implementation(project(":wvcore"))
    implementation(project(":wvrss"))

    // SwipeRefreshLayout
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.firebase.crashlytics)
    implementation(project(":features:webview"))
    implementation(libs.transport.api)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.01.01"))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.coil.compose)
    //implementation("io.coil-kt.coil3:coil-network-ktor2:3.1.0")
    implementation(libs.coil.network.ktor3)

    implementation(libs.firebase.messaging)
    implementation(libs.onesignal)
    implementation(libs.accompanist.webview)
    implementation(libs.google.firebase.crashlytics)
} 