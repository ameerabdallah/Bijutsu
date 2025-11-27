pluginManagement {
    val quarkusPluginVersion: String by settings
    val quarkusPluginId: String by settings
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
    plugins {
        id(quarkusPluginId) version quarkusPluginVersion
    }
}

include(":bijutsu-domain")
include(":bijutsu")
include(":bijutsu-jooq:codegen")
include(":bijutsu-jooq:repository")

rootProject.name = "Bijutsu"