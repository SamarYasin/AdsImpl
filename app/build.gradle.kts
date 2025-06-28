plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.serialization)
    alias(libs.plugins.room)
}

android {
    namespace = "com.example.adsimpl"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.adsimpl"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isDebuggable = false
            isShrinkResources = false
            isDefault = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isDebuggable = true
            isShrinkResources = false
            isDefault = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Ad Unit IDs for testing
            resValue("string", "native_small_ad", "ca-app-pub-3940256099942544/2247696110")
            resValue("string", "native_medium_ad", "ca-app-pub-3940256099942544/2247696110")
            resValue("string", "interstitial_ad", "ca-app-pub-3940256099942544/1033173712")
            resValue("string", "banner_ad", "ca-app-pub-3940256099942544/9214589741")
            resValue("string", "rewarded_ad", "ca-app-pub-3940256099942544/5224354917")
            resValue("string", "rewarded_interstitial_ad", "ca-app-pub-3940256099942544/5354046379")
            resValue("string", "open_app_ad", "ca-app-pub-3940256099942544/9257395921")

            resValue("string", "remote_config", "ad_impl_remote_config")

        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        // Enabling View Binding and Data Binding
        // View Binding to use feature like including layout files without findViewById
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

}

dependencies {

    // Kotlin Standard Library
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Material Components
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Navigation Components
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Google Play Services Ads
    implementation(libs.play.services.ads)
    implementation(libs.shimmer)

    // Hilt for Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Room for Local Database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Remote Config
    implementation(libs.firebase.config.ktx)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Firebase, Analytics
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    // User Messaging Platform for Consent Management
//    Android API level 21 or higher
    implementation(libs.user.messaging.platform)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}