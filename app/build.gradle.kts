import java.time.LocalDate

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.hilt)
//    alias(libs.plugins.google.services)
//    alias(libs.plugins.firebase.crashlytics)

    id("com.google.devtools.ksp")
}

val majorVersion = 1
val minorVersion = 21
val patchVersion = 4
//val versionSuffix = "beta.1"
val versionSuffix = ""//"" для стабильной версии

val _versionName = if (versionSuffix.isNotBlank()) {
    "$majorVersion.$minorVersion.$patchVersion-$versionSuffix"
} else {
    "$majorVersion.$minorVersion.$patchVersion"
}

val _versionCode = majorVersion * 10000 + minorVersion * 100 + patchVersion
//=====================================================================
android {

    lint {
        baseline = file("lint-baseline.xml")
    }

    namespace = "net.lds.online"
    compileSdk = 36


    defaultConfig {
        //applicationId = "net.lds.online"
        //applicationId = "net.lds.online.webview"
        applicationId = "net.lds.online.beta"

        minSdk = 21
        targetSdk = libs.versions.android.targetSdk.get().toInt()

        versionCode = _versionCode
        versionName = _versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        setProperty("archivesBaseName", "$applicationId-$_versionName")
    }

    signingConfigs {
        create("release") {
            if (project.hasProperty("KEYSTORE")) {
                storeFile = file(project.properties["KEYSTORE"] as String)
                storePassword = project.properties["KEYSTORE_PASSWORD"] as String
                keyAlias = project.properties["KEY_ALIAS"] as String
                keyPassword = project.properties["KEY_PASSWORD"] as String
            }
        }
    }
    buildTypes {

        buildTypes {
            getByName("debug") {
                //applicationIdSuffix = ".debug" // добавляет к applicationId суффикс
                versionNameSuffix = "-debug-WebView"   // добавляет к versionName суффикс
                //signingConfig = signingConfigs.getByName("release")
            }
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                versionNameSuffix = "-release"
                signingConfig = signingConfigs.getByName("release")
            }
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
    implementation(platform(libs.androidx.compose.bom))
    implementation(project(":wvcore"))
    implementation(project(":wvrss"))

    // SwipeRefreshLayout
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.firebase.crashlytics)
    implementation(project(":features:webview"))
    implementation(libs.transport.api)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
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
    //implementation("io.coil-kt.coil3:coil-network-ktor2:3.1.0")
    implementation(libs.coil.network.ktor3)

    implementation(libs.firebase.messaging)
    implementation(libs.onesignal)
    implementation(libs.accompanist.webview)
    implementation(libs.google.firebase.crashlytics)
} 