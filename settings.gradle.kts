/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 * For more detailed information on multi-project builds, please refer to https://docs.gradle.org/8.9/userguide/multi_project_builds.html in the Gradle documentation.
 */

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "request-logger-app"
include(
    "app",

    //common
    "common:security",
    "common:webservice",
    "common:repository-data-model",
    "common:domain",

    //verve domain
    "tracker:domain",
    "tracker:repository",
    "tracker:webservice",
    "tracker:webservice-contracts"
)
