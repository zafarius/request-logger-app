/*
 * This file was generated by the Gradle 'init' task.
 */
project.version = project.findProperty("projVersion") ?: "0.1.0-SNAPSHOT"

plugins {
    id("buildlogic.java-library-conventions")
    alias(libs.plugins.lombok)
}

dependencies {
    implementation(libs.spring.boot.starter.artemis)
    implementation(libs.org.messaginghub.pooled.jms)
    implementation(libs.jakarta.jms.api)
    implementation(libs.org.apache.activemq.server)
    implementation(libs.jakarta.validation)
    implementation(libs.jackson.databind)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.org.awaitility)
}

//rename jar file
tasks.getByName<Jar>("jar") {
    archiveBaseName = "common-jms-config"
}