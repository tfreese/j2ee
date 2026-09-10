// Execute Tasks in SubModule: gradle MODUL:clean build
plugins {
    id("de.freese.gradle.conventions").apply(false)
    id("io.spring.dependency-management").apply(false)

    // build/generateOpenApiSpec
    id("io.smallrye.openapi").apply(false)

    // https://github.com/OpenAPITools/openapi-generator/
    id("org.openapi.generator").apply(false)

    id("project-report")
}

allprojects {
    plugins.apply("base")

    tasks.named<Delete>("clean") {
        delete("bin")
        delete("logs")
        delete("out")
        delete("target")
    }
}

subprojects {
    plugins.apply("de.freese.gradle.conventions")
    plugins.apply("io.spring.dependency-management")

    extensions.configure(io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension::class.java) {
        imports {
            mavenBom("com.amazonaws:aws-java-sdk-bom:" + property("version_amazonAws"))
            mavenBom("org.springframework.boot:spring-boot-dependencies:" + property("version_springBoot"))
        }

        dependencies {
            dependency("com.esotericsoftware:kryo:" + property("version_kryo"))
            dependency("io.openliberty.api:io.openliberty.transaction:" + property("version_openlibertyApi"))
            dependency("jakarta.platform:jakarta.jakartaee-api:" + property("version_jakartaApi"))
            dependency("org.eclipse.microprofile.openapi:microprofile-openapi-api:" + property("version_microprofileOpenapiApi"))
            // dependency("org.glassfish.jersey.connectors:jersey-jnh-connector:" + dependencyManagement.importedProperties["jersey.version"])
            dependency("org.primefaces:primefaces:" + property("version_primefaces"))
            dependency("org.primefaces:primefaces-themes:" + property("version_primefacesThemes"))
            dependency("org.primefaces.extensions:primefaces-extensions:" + property("version_primefacesExtensions"))
        }
    }

    plugins.withType<JavaPlugin> {
        dependencies {
            add("testImplementation", "org.junit.jupiter:junit-jupiter")
            add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
        }
    }
}

