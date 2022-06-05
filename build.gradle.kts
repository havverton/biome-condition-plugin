import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `java-library`
  `maven-publish`
  id("io.papermc.paperweight.userdev") version "1.3.2"
  id("xyz.jpenilla.run-paper") version "1.0.6" // Adds runServer and runMojangMappedServer tasks for testing
  id("net.minecrell.plugin-yml.bukkit") version "0.5.1" // Generates plugin.yml

  // Shades and relocates dependencies into our plugin jar. See https://imperceptiblethoughts.com/shadow/introduction/
  id("com.github.johnrengelman.shadow") version "7.1.0"
  kotlin("jvm") version "1.6.10"
}

group = "io.recraft"
version = "1.0.1-SNAPSHOT"
description = "MythicMobs addon that adds vanilla biomes support"

java {
  // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
kotlin {
  jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
  }
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = "io.recraft"
      artifactId = "biome-condition"
      from(components["java"])
    }
    repositories {
      maven {
        url = uri("https://nexus.recraft.pro/repository/maven-snapshots/")
        credentials {
          username = "admin"
          password = System.getenv()["NEXUS_PASSWORD"]
        }
      }
    }
  }
}


dependencies {
  paperDevBundle("1.18.2-R0.1-SNAPSHOT")
  // paperweightDevBundle("com.example.paperfork", "1.18-R0.1-SNAPSHOT")

  // You will need to manually specify the full dependency if using the groovy gradle dsl
  // (paperDevBundle and paperweightDevBundle functions do not work in groovy)
  // paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:1.18-R0.1-SNAPSHOT")

  // Shadow will include the runtimeClasspath by default, which implementation adds to.
  // Dependencies you don't want to include go in the compileOnly configuration.
  // Make sure to relocate shaded dependencies!

  compileOnly("io.lumine:Mythic-Dist:5.1.0-SNAPSHOT")
  // Kotlin StdLib
  compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")
  // Reflection stuff like class.createInstance()
  compileOnly("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
  // Kotlin's coroutines (Mostly for mongodb)
  compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
}

tasks {
  // Configure reobfJar to run when invoking the build task
  build {
    dependsOn(reobfJar)
    dependsOn(shadowJar)
  }

  reobfJar {
    outputJar.set(layout.buildDirectory.file("release/recraft-${project.name}-${project.version}.jar"))
  }

  compileJava {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

    // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
    // See https://openjdk.java.net/jeps/247 for more information.
    options.release.set(17)
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
  }
  processResources {
    filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
  }

  reobfJar {
    outputJar.set(layout.buildDirectory.file("release/recraft-${project.name}-${project.version}.jar"))
  }

  shadowJar {
    // helper function to relocate a package into our package
    fun reloc(pkg: String) = relocate(pkg, "io.recraft.biomeCondition.dependency.$pkg")

    // relocate cloud and it's transitive dependencies
    reloc("cloud.commandframework")
    reloc("io.leangen.geantyref") }
}

// Configure plugin.yml generation
bukkit {
  load = BukkitPluginDescription.PluginLoadOrder.STARTUP
  main = "io.recraft.biomeCondition.BiomeConditionPlugin"
  apiVersion = "1.18"
  authors = listOf("Author")
}

repositories {
  mavenCentral()
  maven("https://mvn.lumine.io/repository/maven-public")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
  jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
  jvmTarget = "17"
}
