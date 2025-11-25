plugins {
    id("com.android.application")
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
    implementation(
        libs.media3.exoplayer
    )
    implementation(
        libs.media3.ui
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
    implementation(
        "com.google.android.material:material:1.12.0"
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
        "androidx.camera:camera-video:${cameraxVersion}"
    )

    // 👇 Esta es la del Card Swipe
    implementation(
        "com.github.yuyakaido:CardStackView:v2.3.4"
    )
    implementation(
        "com.github.bumptech.glide:glide:4.13.2"
    )
    implementation(
        "androidx.camera:camera-extensions:${cameraxVersion}"
    )

    //Yuyakaido CardStackView
    implementation(
        "com.yuyakaido.android:card-stack-view:2.3.4"
    )


}
