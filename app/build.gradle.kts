plugins {
    id("com.android.application")
}

android {
    namespace = "binhntph28014.fpoly.gophoneapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "binhntph28014.fpoly.gophoneapplication"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation ("com.etebarian:meow-bottom-navigation:1.2.0")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.61")

    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.airbnb.android:lottie:5.2.0")


    implementation ("io.github.chaosleung:pinview:1.4.4")

    implementation ("io.jsonwebtoken:jjwt-api:0.11.2")
    implementation ("io.jsonwebtoken:jjwt-impl:0.11.2")
    implementation ("io.jsonwebtoken:jjwt-jackson:0.11.2")

    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")

    implementation("com.google.ai.client.generativeai:generativeai:0.3.0")

    implementation("com.google.guava:guava:31.0.1-android")

    implementation ("com.google.android.gms:play-services-auth:20.4.1")

    implementation ("io.socket:socket.io-client:2.0.0") {

    }
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

}