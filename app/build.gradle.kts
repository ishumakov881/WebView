import java.time.LocalDate

import java.util.Properties
import java.text.SimpleDateFormat
import java.util.Date

fun versionCodeDate(): Int {
    val dateFormat = SimpleDateFormat("yyMMdd")
    return dateFormat.format(Date()).toInt()
}

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


android {

    lint {
        baseline = file("lint-baseline.xml")
    }

    //namespace = "com.knopka.kz"
    namespace = "com.knopka.kz"

    compileSdk = libs.versions.android.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.android.buildTools.get()

    val code = versionCodeDate()

    defaultConfig {
        //applicationId = "com.knopka.kz"
        applicationId = "com.example.app"

        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()

        versionCode = code
        versionName = "1.3.$code"

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
            keyAlias = "release"
            keyPassword = "release"
            storeFile = file("keystore/pozitiv.jks")
            storePassword = "release"
        }

        create("lordseriala") {
            keyAlias = "release"
            keyPassword = "release"
            storeFile = file("keystore/lordseriala.jks")
            storePassword = "release"
        }

    }

    buildTypes {

        getByName("debug") {
            //applicationIdSuffix = ".debug"
            //signingConfig = signingConfigs.getByName("config")
        }

        getByName("release") {
            isMinifyEnabled = true
            //signingConfig = signingConfigs.getByName("config")

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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



    flavorDimensions += "client"

    productFlavors {

        create("knopka") {
            dimension = "client"
            applicationId = "com.knopka.kz"
            versionNameSuffix = "-knopka"
            resValue("string", "app_name", "knopka.kz")
            signingConfig = signingConfigs.getByName("knopka")
        }


        create("lordseriala") {
            dimension = "client"
            applicationId = "life.lordseriala.android"
            versionNameSuffix = "-lordseriala"
            resValue("string", "app_name", "LORDSERIALA")
            resValue("bool", "show_onboarding", "false")

            signingConfig = signingConfigs.getByName("lordseriala")
        }

        create("pozitiv") {
            dimension = "client"
            applicationId = "com.appbery.appberypositiverf"
            //versionNameSuffix = "-positiverf"
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

    implementation(libs.androidx.compose.material3)
    //implementation(platform(libs.androidx.compose.bom))
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
    //androidTestImplementation(platform(libs.androidx.compose.bom))
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

    implementation(libs.bootstrapiconscompose)
    // SplashScreen
    implementation(libs.androidx.core.splashscreen)

    // https://mvnrepository.com/artifact/androidx.annotation/annotation-experimental
    runtimeOnly(libs.androidx.annotation.experimental)
    implementation("androidx.activity:activity-ktx:1.11.0-rc01")
} 