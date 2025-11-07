import org.flywaydb.core.Flyway
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

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
val jooqVersion: String by project
val jooqGeneratedSourcesDir = "${projectDir}/build/classes/java/quarkus-generated-sources/jooq"

buildscript {
    val jooqVersion: String by project
    val postgresqlVersion: String by project
    val testcontainersVersion: String by project

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jooq:jooq-codegen:$jooqVersion")
        classpath("org.jooq:jooq-meta:$jooqVersion")
        classpath("org.postgresql:postgresql:$postgresqlVersion")
        classpath(platform("org.testcontainers:testcontainers-bom:$testcontainersVersion"))
        classpath("org.testcontainers:testcontainers")
        classpath("org.testcontainers:postgresql")
        classpath("org.flywaydb:flyway-core")
        classpath("org.flywaydb:flyway-database-postgresql")
    }
}

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-client-jackson")
    implementation("io.quarkiverse.openapi.generator:quarkus-openapi-generator:2.13.0-lts")
    implementation("org.apache.commons:commons-text:1.14.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.20.1")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.quarkus:quarkus-junit5-mockito")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.mockito:mockito-core:5.20.0") // core library

    // Flyway specific dependencies
    implementation("io.quarkus:quarkus-flyway")
    // JDBC driver dependencies
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    // Flyway PostgreSQL specific dependencies
    implementation("org.flywaydb:flyway-database-postgresql")

    // jOOQ dependencies
    implementation("org.jooq:jooq:$jooqVersion")
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
            srcDir(jooqGeneratedSourcesDir)
        }
    }
}
tasks.register("jooqCodegen") {
    group = "jooq"
    description = "Generate jOOQ sources using Testcontainers"

    doLast {
        // Start PostgreSQL container
        val postgres = PostgreSQLContainer(DockerImageName.parse("postgres:18-alpine"))
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withExposedPorts(5432)

        postgres.start()

        try {
            // Run Flyway migrations
            val flyway = Flyway.configure()
                .dataSource(postgres.jdbcUrl, postgres.username, postgres.password)
                .locations("filesystem:${projectDir}/src/main/resources/db/migrations")
                .load()

            flyway.migrate()

            // Generate jOOQ code
            val configuration = Configuration()
                .withJdbc(
                    Jdbc()
                        .withDriver("org.postgresql.Driver")
                        .withUrl(postgres.jdbcUrl)
                        .withUser(postgres.username)
                        .withPassword(postgres.password)
                )
                .withGenerator(
                    Generator()
                        .withDatabase(
                            Database()
                                .withName("org.jooq.meta.postgres.PostgresDatabase")
                                .withIncludes(".*")
                                .withExcludes("")
                                .withInputSchema("public")
                        ).withGenerate(
                            Generate()
                                .withRecords(true)
                                .withPojos(true)
                                .withFluentSetters(true)
                        )
                        .withTarget(
                            Target()
                                .withPackageName("com.ameerdev.jooq")
                                .withDirectory(jooqGeneratedSourcesDir)
                        )
                )

            GenerationTool.generate(configuration)

        } finally {
            postgres.stop()
        }
    }
}

tasks.named("quarkusGenerateCode") {
    dependsOn("jooqCodegen")
}
