plugins {
    kotlin("jvm") version "1.9.25"
    application
}

group = "com.github.mdombrovsky"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.rometools:rome:1.18.0")
    implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation("com.github.kotlin-telegram-bot.kotlin-telegram-bot:dispatcher:6.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.json:json:20250107")
    implementation("org.jsoup:jsoup:1.20.1")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("deploy")

application {
    mainClass.set(
        project.findProperty("mainClass")?.toString()
    )
}