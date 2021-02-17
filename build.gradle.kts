
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30"
    kotlin("kapt") version "1.4.30"
    application
}

group = "org.andriesfc.rankmeb"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val javacTarget = JavaVersion.VERSION_1_8.toString()

tasks.withType<JavaCompile> {
    sourceCompatibility = javacTarget
    targetCompatibility = javacTarget
}

application {
    mainClass.set("org.andriesfc.rankmeb.AppKt")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = javacTarget
    }
}


kapt {
    arguments {
        arg("project", "${project.group}/${project.name}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform {
    }
}



dependencies {
    implementation(kotlin("stdlib"))

    // Command line handling
    val picoCli = "4.6.1"
    implementation("info.picocli:picocli:$picoCli")
    kapt("info.picocli:picocli-codegen:$picoCli")

    // JUnit5
    val junitVersion = "5.7.1"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    // We want some ids
    implementation("org.hashids:hashids:1.0.3")
}
