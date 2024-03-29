plugins {
    id("java")
    // ShadowJar (https://github.com/johnrengelman/shadow/releases)
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.zax71"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.panda-lang.org/releases")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-oss-snapshots"
    }
}

dependencies {
    // Minestom
    implementation("dev.hollowcube:minestom-ce:010fe985bb")

    // Polar
    implementation("dev.hollowcube:polar:1.3.1")

    // Kyori stuff (Adventure)
    implementation("net.kyori:adventure-text-serializer-plain:4.13.1")
    implementation("net.kyori:adventure-text-minimessage:4.13.1")
    implementation("net.kyori:adventure-text-serializer-ansi:4.14.0-SNAPSHOT")

    // Configuration API
    implementation("org.spongepowered:configurate-hocon:4.1.2")

    // LiteCommands (command framework)
    implementation("dev.rollczi.litecommands:core:2.8.7")
    implementation("dev.rollczi.litecommands:minestom:2.8.7")

    // Logger
    implementation("ch.qos.logback:logback-classic:1.3.3")

    // Redis (Jedis)
    implementation("redis.clients:jedis:4.3.0")

    // Endercube common lib
    implementation("com.github.ender-cube:endercubecommon:1e8da50caf")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "me.zax71.lightHub.Main"
        }
    }
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("") // Prevent the -all suffix
    }
}