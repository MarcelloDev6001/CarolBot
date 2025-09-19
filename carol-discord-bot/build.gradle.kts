plugins {
    id("java")
    kotlin("plugin.serialization") version "latest.release"
    kotlin("jvm")
}

group = "com.marc.discordbot.carol"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("net.dv8tion:JDA:5.6.1")
    implementation("org.reflections:reflections:0.10.2")

    //database
    implementation("io.ktor:ktor-client-cio:latest.release")
    implementation("com.google.firebase:firebase-admin:9.2.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(24)
}