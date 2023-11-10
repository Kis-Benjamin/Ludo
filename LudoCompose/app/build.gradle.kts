@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

repositories.addAll(project.rootProject.repositories)

android {
    namespace = "hu.bme.aut.android.ludocompose"
    compileSdk = 34

    defaultConfig {
        applicationId = "hu.bme.aut.android.ludocompose"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        isCoreLibraryDesugaringEnabled = true
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
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
    enableExperimentalClasspathAggregation = true
}

dependencies {
    // Android-Kotlin
    implementation(libs.core.ktx)
    // Android-Compose
    implementation(libs.bundles.lifecycle)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    // Room
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
    // Hilt
    implementation(libs.bundles.hilt)
    kapt(libs.dagger.hilt.compiler)
    // Kotlin extensions
    implementation(libs.datetime)
    // Security
    implementation(libs.bundles.security)
    // Network
    implementation(libs.bundles.network)
    // Moshi Kotlin
    implementation(libs.bundles.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)
    // Util
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    // Test
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.test)
    // Debug
    debugImplementation(libs.bundles.debug)
}
