plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
}

group = "me.grian"
version = "1.0-SNAPSHOT"

val ktor_version: String by project
val kord_version: String by project

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation("io.ktor:ktor-client-cio-jvm:3.1.3")
    testImplementation(kotlin("test"))
    implementation("dev.kord:kord-core:${kord_version}")
    implementation("io.ktor:ktor-client-core:${ktor_version}")
    implementation("io.ktor:ktor-client-cio:${ktor_version}")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    implementation("io.github.classgraph:classgraph:4.8.179")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}