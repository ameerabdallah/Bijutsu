import io.quarkus.gradle.tasks.QuarkusDev

plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val lombokVersion: String by project

val mockitoAgent = configurations.create("mockitoAgent")

dependencies {
    // Module dependencies
    implementation(project(":bijutsu-domain"))
    implementation(project(":bijutsu-jooq:repository"))

    // Quarkus platform
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-cache")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-rest-client-jackson")
    implementation("io.quarkiverse.openapi.generator:quarkus-openapi-generator:2.13.0-lts")
    implementation("org.apache.commons:commons-text:1.14.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.20.1")
    implementation("org.jetbrains:annotations:26.0.2-1")
    implementation("io.quarkus:quarkus-smallrye-context-propagation")

    // Testing
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.quarkus:quarkus-junit5-mockito")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation(libs.mockito)
    mockitoAgent(libs.mockito) { isTransitive = false }

    // Flyway for database migrations at runtime
    implementation("io.quarkus:quarkus-flyway")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("org.flywaydb:flyway-database-postgresql")

    // Lombok for models
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
}

group = "com.ameerdev"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
    jvmArgs(
        "--add-opens",
        "java.base/java.lang=ALL-UNNAMED",
        "-Xshare:off",
        "-javaagent:${mockitoAgent.asPath}"
    )
    useJUnitPlatform {
        excludeTags("integration")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.register("integrationTest", Test::class) {
    useJUnitPlatform {
        includeTags("integration")
    }
    shouldRunAfter(Test::class)
}

tasks.named<QuarkusDev>("quarkusDev") {
    jvmArgs = listOf(
        "--add-opens", "java.base/java.nio=ALL-UNNAMED",
        "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED",
        "-Dio.netty.noUnsafe=true"
    )
}

sourceSets {
    main {
        java {
            srcDirs("build/classes/java/quarkus-generated-sources/open-api")
        }
    }
}
