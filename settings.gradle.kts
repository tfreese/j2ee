// Can not be configured by Conventions-Plugin.
pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }

    val versionMyJavaConventionPlugin = providers.gradleProperty("version_myJavaConventionPlugin")
    val versionSpringDependencyManagementPlugin = providers.gradleProperty("version_springDependencyManagementPlugin")
    val versionLibertyGradlePlugin = providers.gradleProperty("version_libertyGradlePlugin")
    val versionIoSmallryeOpenapiGradlePlugin = providers.gradleProperty("version_ioSmallryeOpenapiGradlePlugin")
    val versionOpenApiGeneratorGradlePlugin = providers.gradleProperty("version_openApiGeneratorGradlePlugin")

    plugins {
        id("de.freese.gradle.conventions").version(versionMyJavaConventionPlugin).apply(false)
        id("io.spring.dependency-management").version(versionSpringDependencyManagementPlugin).apply(false)

        id("io.openliberty.tools.gradle.Liberty").version(versionLibertyGradlePlugin).apply(false)

        // build/generateOpenApiSpec
        id("io.smallrye.openapi").version(versionIoSmallryeOpenapiGradlePlugin).apply(false)

        // https://github.com/OpenAPITools/openapi-generator/
        id("org.openapi.generator").version(versionOpenApiGeneratorGradlePlugin).apply(false)

        id("project-report")
    }
}

// Without rootProject.name the Name of the Projekt-Directory is used.
// rootProject.name = "j2ee"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

include("cloudsessions")
include("cloudsessions-integration")
include("jcache")
include("jpa")
include("liberty:liberty-demo")
include("liberty:liberty-login")
include("liberty:liberty-spring")
