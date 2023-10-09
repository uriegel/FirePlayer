plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
}

android {
    namespace = "de.uriegel.fireplayer"
    compileSdk = 34

    signingConfigs {
        create("signing") {
            storeFile = file("/home/uwe/Dokumente/Entwicklung/AndroidKeyStore/keystore.jks")
            storePassword = extra["ANDROID_STORE_PASSWORD"].toString()
            keyAlias = "androidKey"
            keyPassword = extra["ANDROID_KEY_PASSWORD"].toString()
        }
    }

    defaultConfig {
        applicationId = "de.uriegel.fireplayer"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("signing")
        }
        debug {
            signingConfig = signingConfigs.getByName("signing")
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/*"
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.exifinterface:exifinterface:1.3.6")
    implementation("androidx.media3:media3-exoplayer:1.1.1")
    implementation("androidx.media3:media3-ui:1.1.1")
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    implementation("com.google.accompanist:accompanist-pager:0.28.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    kapt("androidx.room:room-compiler:2.5.2")
    annotationProcessor("androidx.room:room-compiler:2.5.2")
    val composeBom = platform("androidx.compose:compose-bom:2023.01.00")
    implementation(composeBom)
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")
}
