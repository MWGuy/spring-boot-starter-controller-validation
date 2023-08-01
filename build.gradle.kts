plugins {
    id("maven-publish")
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
}

group = "com.github.MWGuy"
version = "1.0"

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
            groupId = "com.github.MWGuy"
            artifactId = "spring-boot-starter-controller-validation"
            version = "1.0"

            from(components["java"])
        }
    }
}
