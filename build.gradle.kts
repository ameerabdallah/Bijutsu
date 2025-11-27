plugins {
    id("org.jlleitschuh.gradle.ktlint") version "13.0.0"
}

tasks.withType<Wrapper> {
    gradleVersion = "9.1.0"
}