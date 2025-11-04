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
val lombokVersion = "1.18.30"

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-client-jackson")
    implementation("io.quarkiverse.openapi.generator:quarkus-openapi-generator:2.12.1")
    implementation("org.apache.commons:commons-text:1.14.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.20.1")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.quarkus:quarkus-junit5-mockito")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.mockito:mockito-core:5.20.0") // core library
}

group = "com.ameerdev"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED", "-Xshare:off")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.withType<Test> {
    useJUnitPlatform {
        excludeTags("integration")
    }
}

tasks.register("integrationTest", Test::class) {
    useJUnitPlatform {
        includeTags("integration")
    }
    shouldRunAfter(Test::class)
}

sourceSets {
    main {
        java {
            srcDirs("build/classes/java/quarkus-generated-sources/open-api")
        }
    }
}
