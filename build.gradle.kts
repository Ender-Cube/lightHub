import org.jetbrains.kotlin.com.intellij.openapi.vfs.StandardFileSystems.jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar


plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.22"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("java")
}

group = "me.zax71.lightHub"
version = "1.0.0"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.22")

    // Server API
    implementation("com.github.Minestom:Minestom:809d9516b2")

    // Text pharsing
    implementation("net.kyori:adventure-text-minimessage:4.12.0")

    // Config util
    implementation("com.github.simplix-softworks:simplixstorage:3.2.4")

    // Logging util
    implementation("org.tinylog:tinylog-api-kotlin:2.5.0")
    implementation("org.tinylog:tinylog-impl:2.5.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

}

tasks.withType<ShadowJar> {
    manifest {
        attributes["Main-Class"] = "me.zax71.lightHub.MainKt"
    }
}

tasks.named("build") {
    dependsOn(tasks.named("shadowJar"))
}