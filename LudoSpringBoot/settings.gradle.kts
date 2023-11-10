pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
    maven(url = "https://repo.spring.io/release")
    maven(url = "https://repo.spring.io/milestone")
  }
}

rootProject.name = "ludo-server"
include(":game-server")
