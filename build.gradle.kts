plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    alias(libs.plugins.google.services) apply false
    //alias(libs.plugins.firebase.crashlytics) apply false
}