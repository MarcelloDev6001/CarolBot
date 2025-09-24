plugins {
    id("java")
    id("application")
    kotlin("plugin.serialization") version "latest.release"
    kotlin("jvm")
}

group = "com.marc.discordbot.carol"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    // JDA stuffs
    implementation("net.dv8tion:JDA:5.6.1")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("com.github.walkyst:lavaplayer-fork:1.4.3") // to play audio on a voice channel

    // other stuffs
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.github.oshi:oshi-core:6.6.5")

    // database
    implementation("com.google.firebase:firebase-admin:9.2.0")

    // added by default from IntelliJ IDEA
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

application {
    mainClass.set("com.marc.discordbot.carol.CarolLauncher")
}

tasks.named<JavaExec>("run") {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}
