plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

// Configure JVM target compatibility (Android compatible)
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.singkhmer.transliterator.MainKt")
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
