import java.io.File
import java.io.FileInputStream
import java.util.*
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}
val prop = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "local.properties")))
}
android {
    namespace = "com.bogarsoft.babynames"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.bogarsoft.babynames"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        prop.forEach { (key, value) ->
            if(!key.toString().equals("sdk.dir"))
                buildConfigField("String", key.toString(), value.toString())
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    val lifecycle_version = "2.6.1"
    val retrofit_version = "2.9.0"
    val logging_version = "5.0.0-alpha.5"
    val hilt_version = "2.44.2"

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.fragment:fragment-ktx:1.5.7")

    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")


    // Hilt
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")

    // interceptor
    implementation("com.squareup.okhttp3:okhttp:$logging_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$logging_version")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")



    // Coil
    implementation("io.coil-kt:coil:2.4.0")

    //veil
    //shimmer effect
    implementation("com.github.skydoves:androidveil:1.1.2")


    // https://mvnrepository.com/artifact/com.google.firebase/firebase-bom
    implementation(platform("com.google.firebase:firebase-bom:32.0.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx:23.1.2")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    //SP
    implementation("com.github.cioccarellia:ksprefs:2.3.2")

    implementation("com.github.kamaravichow:admob-templates-android:1.1.3")

    //admob
    implementation("com.google.android.gms:play-services-ads:22.1.0")


    val lottieVersion = "6.0.0"
    implementation("com.airbnb.android:lottie:$lottieVersion")
}