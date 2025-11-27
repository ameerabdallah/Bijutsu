plugins {
    java
    id("io.quarkus")
    id("org.kordamp.gradle.jandex") version "2.3.0"
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val jooqVersion: String by project

dependencies {
    // Domain module (public API)
    implementation(project(":bijutsu-domain"))

    // jOOQ generated code (internal implementation detail)
    implementation(project(":bijutsu-jooq:codegen"))

    // Quarkus platform for CDI and other dependencies
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    compileOnly("io.quarkus:quarkus-core")
    compileOnly("io.quarkus:quarkus-arc")
    compileOnly("io.quarkus:quarkus-agroal")

    // CDI annotations for dependency injection
    compileOnly("jakarta.enterprise:jakarta.enterprise.cdi-api:4.1.0")
    compileOnly("jakarta.inject:jakarta.inject-api:2.0.1")

    // Annotations
    implementation("org.jetbrains:annotations:26.0.2-1")
    implementation("org.jooq:jooq:$jooqVersion")
}

group = "com.ameerdev"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}