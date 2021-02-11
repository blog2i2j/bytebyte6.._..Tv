plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("com.github.dcendents.android-maven")
}

group = "com.github.bytebyte6"

android {
    compileSdkVersion(Versions.compile_sdk)
    buildToolsVersion(Versions.buildToolsVersion)

    defaultConfig {
        minSdkVersion(Versions.min_sdk)
        targetSdkVersion(Versions.target_sdk)
        versionCode = Versions.versionCode
        versionName = Versions.versionName

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
        consumerProguardFiles("consumer-proguard-rules.pro")
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

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":lib_dependency"))
    androidTestImplementation(Libs.TEST_ESPRESSO_CORE)
    androidTestImplementation(Libs.TEST_ESPRESSO_CONTRIB)
    androidTestImplementation(Libs.TEST_ESPRESSO_UI_AUTOMATOR)
    androidTestImplementation(Libs.TEST_ESPRESSO_INTEGRATION)
    androidTestImplementation(Libs.TEST_CORE)
    androidTestImplementation(Libs.TEST_RUNNER)
    androidTestImplementation(Libs.TEST_RULES)
    androidTestImplementation(Libs.TEST_KOIN)
    androidTestImplementation(Libs.TEST_ARCH_TESTING)
    androidTestImplementation(Libs.TEST_JUNIT)
    androidTestImplementation(Libs.TEST_JUNIT_KTX)
}
