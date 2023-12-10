// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  id("com.android.application") version "8.1.4" apply false
  id("org.jetbrains.kotlin.android") version "1.8.10" apply false
  id("org.sonarqube") version "4.4.1.3373"
}

sonar {
  properties {
    property(
      "sonar.projectKey",
      "Tu-Varna-2019_Tu-Varna-Masters-Winter-AndroidKotlin-Project-Chat_AYwcmzPxkyMQ8V9j1VQJ"
    )
  }
}
