plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}


android {
    namespace =
        "com.emir201.dualshare"
    compileSdk =
        36

    defaultConfig {
        applicationId =
            "com.emir201.dualshare"
        minSdk =
            24
        targetSdk =
            36
        versionCode =
            1
        versionName =
            "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled =
                false
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility =
            JavaVersion.VERSION_11
        targetCompatibility =
            JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding =
            true
    }
}

dependencies {

    // Librerías del catálogo (normal)
    implementation(
        libs.appcompat
    )
    implementation(
        libs.material
    )
    implementation(
        libs.activity
    )
    implementation(
        libs.constraintlayout
    )
    implementation(
        libs.recyclerview
    )
    implementation(
        libs.cardview
    )

    testImplementation(
        libs.junit
    )
    androidTestImplementation(
        libs.ext.junit
    )
    androidTestImplementation(
        libs.espresso.core
    )

    // UI
    implementation(
        "com.airbnb.android:lottie:6.1.0"
    )
    implementation(
        "pl.droidsonroids.gif:android-gif-drawable:1.2.29"
    )
    implementation(
        "com.github.bumptech.glide:glide:4.16.0"
    )
    implementation(
        "de.hdodenhof:circleimageview:3.1.0"
    )
    implementation(
        "com.tbuonomo:dotsindicator:4.3"
    )
    implementation(
        "androidx.viewpager2:viewpager2:1.1.0"
    )

    // CameraX
    val cameraxVersion =
        "1.3.4"
    implementation(
        "androidx.camera:camera-core:$cameraxVersion"
    )
    implementation(
        "androidx.camera:camera-camera2:$cameraxVersion"
    )
    implementation(
        "androidx.camera:camera-lifecycle:$cameraxVersion"
    )
    implementation(
        "androidx.camera:camera-view:$cameraxVersion"
    )
    implementation(
        "androidx.camera:camera-extensions:$cameraxVersion"
    )
    implementation(
        "androidx.camera:camera-video:$cameraxVersion"
    )

    // Card Swipe
    implementation(
        "com.github.yuyakaido:CardStackView:2.3.4"
    )

    // ExoPlayer
    implementation(
        "com.google.android.exoplayer:exoplayer:2.19.1"
    )
    implementation(
        "com.google.android.exoplayer:exoplayer-ui:2.19.1"
    )

    // 🔥 Firebase (BOM recomendado)
    implementation(
        platform(
            "com.google.firebase:firebase-bom:33.1.2"
        )
    )

    // Firebase Auth
    implementation(
        "com.google.firebase:firebase-auth"
    )

    // Firebase Storage
    implementation(
        "com.google.firebase:firebase-storage"
    )

    // 🔥 Facebook Login
    implementation(
        "com.facebook.android:facebook-login:latest.release"
    )
    // 🔥 Google Sign-In
    implementation(
        "com.google.android.gms:play-services-auth:20.7.0"
    )
    implementation(
        "com.github.bumptech.glide:glide:4.15.1"
    )
    annotationProcessor(
        "com.github.bumptech.glide:compiler:4.15.1"
    )

}
