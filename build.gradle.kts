plugins {
    id("org.jlleitschuh.gradle.ktlint") version "13.0.0"
    checkstyle
}

allprojects {
    apply(plugin = "checkstyle")

    tasks.withType<Checkstyle>().configureEach {
        reports {
            xml.required.set(false)
            html.required.set(true)
        }
        exclude("**/build/**")
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "9.1.0"
}