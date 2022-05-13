import buildConfig.*
import buildConfig.Deps.addUnitTest
import buildConfig.Deps.implementationCompose

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = appConfig.compileSdk

    defaultConfig {
        applicationId = appConfig.applicationId
        minSdk = appConfig.minSdk
        targetSdk = appConfig.targetSdk
        versionCode = appConfig.versionCode
        versionName = appConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes.all {
        buildConfigField("String", "REST_CAT_API_BASE_URL", "\"https://api.thecatapi.com/v1/\"")
        buildConfigField("String", "REST_CAT_API_KEY", "\"0d627083-3eb0-4da7-b38e-1eba46662a91\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

dependencies {
    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.Material.material)
    implementationCompose()

    // DI
    implementation(Deps.Hilt.hiltAndroid)
    kapt(Deps.Hilt.hiltCompiler)

    // Networking
    implementation(Deps.OkHttp.okhttp)
    implementation(Deps.OkHttp.loggingInterceptor)
    implementation(Deps.Retrofit.retrofit)
    implementation(Deps.Retrofit.retrofitConverters)
    implementation(Deps.Retrofit.converterMoshi)
    implementation(Deps.Moshi.moshi)
    implementation(Deps.Moshi.moshiAdapters)
    kapt(Deps.Moshi.moshiKotlinCodegen)

    // Coroutines
    implementation(Deps.Coroutines.core)
    implementation(Deps.Coroutines.android)

    // Test
    addUnitTest()
}