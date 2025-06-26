plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.serialization)
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

            resValue("string", "remote_config", "ad_impl_remote_config")

        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures{
        viewBinding = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation (libs.play.services.ads)
    implementation(libs.shimmer)

    implementation( libs.hilt.android)
    ksp (libs.hilt.compiler)

    implementation (libs.firebase.config.ktx)

    implementation (libs.kotlinx.serialization.json)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}