pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "sing-khmer-keyboard"

include("transliterator-core", "transliterator-cli", "android-keyboard")
