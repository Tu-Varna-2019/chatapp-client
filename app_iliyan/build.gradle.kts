plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("org.sonarqube") version "4.4.1.3373"
  kotlin("plugin.serialization") version "2.0.0"
}

sonar {
  properties {
    property(
      "sonar.projectKey",
      "Tu-Varna-2019_Tu-Varna-Masters-Winter-AndroidKotlin-Project-Chat_AYwcmzPxkyMQ8V9j1VQJ"
    )
  }
}

android {
  namespace = "com.example.app_iliyan"
  compileSdk = 34

  buildFeatures { buildConfig = true }

  packaging { resources { excludes.add("META-INF/DEPENDENCIES") } }

  defaultConfig {
    applicationId = "com.example.app_iliyan"
    minSdk = 26
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"
    buildConfigField("String", "SERVER_ADDRESS", "\"10.0.2.2\"")
    buildConfigField("int", "PORT", "8081")

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables { useSupportLibrary = true }
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
  kotlinOptions { jvmTarget = "1.8" }
  buildFeatures { compose = true }
  composeOptions { kotlinCompilerExtensionVersion = "1.4.3" }
  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
}

dependencies {
  implementation(platform("androidx.compose:compose-bom:2024.06.00"))
  androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))

  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
  implementation("androidx.activity:activity-compose:1.8.2")
  implementation(platform("androidx.compose:compose-bom:2024.06.00"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-graphics")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.11.0")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
  androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
  implementation("org.apache.logging.log4j:log4j-api:2.20.0")
  annotationProcessor("org.apache.logging.log4j:log4j-core:2.20.0")
  implementation("androidx.compose.runtime:runtime-livedata:1.6.8")
  implementation("androidx.work:work-runtime-ktx:2.9.0")
  implementation("io.coil-kt:coil-compose:2.5.0")
}
