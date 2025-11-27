import org.flywaydb.core.Flyway
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

group = "com.ameerdev"
version = "1.0-SNAPSHOT"

plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenCentral()
}

val jooqVersion: String by project
val postgresqlVersion: String by project
val jooqGeneratedSourcesDir = "${projectDir}/build/generated-sources/jooq"
val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

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
    // Only jOOQ and database driver - no domain, no Quarkus
    implementation("org.jooq:jooq:$jooqVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
}
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

// Configure source sets to include generated jOOQ code
sourceSets {
    main {
        java {
            srcDir(jooqGeneratedSourcesDir)
        }
    }
}

// jOOQ code generation task
tasks.register("jooqCodegen") {
    group = "jooq"
    description = "Generate jOOQ sources using Testcontainers"

    doLast {
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
                                .withInputSchema("public")
                        ).withGenerate(
                            Generate()
                                .withDaos(true)
                                .withPojos(true)
                        ).withTarget(
                            Target()
                                .withClean(true)
                                .withPackageName("com.ameerdev.jooq.generated")
                                .withDirectory(jooqGeneratedSourcesDir)
                        )
                )

            GenerationTool.generate(configuration)

        } finally {
            postgres.stop()
        }
    }
}

// Make compileJava depend on jOOQ code generation
//tasks.named("compileJava") {
//    dependsOn("jooqCodegen")
//}