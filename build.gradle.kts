plugins {
    id("org.jlleitschuh.gradle.ktlint") version "13.0.0"
    checkstyle
}

allprojects {
    apply(plugin = "checkstyle")

    checkstyle {
        toolVersion = "12.2.0"
    }

    tasks.withType<Checkstyle>().configureEach {
        reports {
            xml.required.set(false)
            html.required.set(true)
        }
        exclude("**/build/**", "**/generated/**", "**/quarkus-generated-sources/**")
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "9.1.0"
}