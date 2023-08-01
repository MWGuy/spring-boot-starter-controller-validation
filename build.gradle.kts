plugins {
    id("maven-publish")
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
}

group = "com.mwguy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.7.14")
}

kotlin {
    jvmToolchain(8)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.mwguy"
            artifactId = "spring-boot-starter-controller-validation"
            version = "1.0-SNAPSHOT"

            from(components["java"])
        }
    }
}
