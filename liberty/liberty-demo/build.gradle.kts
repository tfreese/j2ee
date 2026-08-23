import java.awt.Desktop
import java.net.URI
import java.util.*

plugins {
    id("war")
    id("ear")

    id("io.openliberty.tools.gradle.Liberty")

    // build/generateOpenApiSpec
    id("io.smallrye.openapi")
    id("org.openapi.generator")
}

// https://openliberty.io/guides/gradle-intro.html#getting-started
description = "Demo for OpenLiberty"

tasks.withType<io.openliberty.tools.gradle.tasks.DeployTask>().configureEach {
    notCompatibleWithConfigurationCache("Das Liberty-Plugin unterstützt den Configuration Cache noch nicht.")
}

tasks.clean {
    delete("/tmp/wlp")
}

val libertyServerHttpPort = "9080"
val libertyServerHttpsPort = "9443"
val libertyContextRoot = name

val destDir = layout.buildDirectory.get().dir("generated-sources")

sourceSets {
    main {
        java {
            srcDir(destDir.dir("src").dir("main").dir("java"))
        }
    }
}

dependencies {
    libertyRuntime("io.openliberty:openliberty-kernel:" + property("version_openliberty"))
    // libertyRuntime("io.openliberty:openliberty-runtime:" + property("version_openliberty"))

    implementation("com.esotericsoftware:kryo")
    implementation("tools.jackson.core:jackson-databind")
    implementation("tools.jackson.jakarta.rs:jackson-jakarta-rs-json-provider")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("com.github.ben-manes.caffeine:jcache")

    implementation("org.hibernate.orm:hibernate-core") {
        exclude(group = "jakarta.activation", module = "*")
        exclude(group = "jakarta.inject", module = "*")
        exclude(group = "jakarta.persistence", module = "*")
        exclude(group = "jakarta.transaction", module = "*")
        // exclude(group = "jakarta.xml.bind", module = "*")
    }

    implementation("org.slf4j:slf4j-api")

    // providedCompile
    compileOnly("io.openliberty.api:io.openliberty.transaction")
    compileOnly("jakarta.platform:jakarta.jakartaee-api")
    compileOnly("org.eclipse.microprofile.openapi:microprofile-openapi-api")

    // runtimeOnly("com.h2database:h2")
    runtimeOnly("org.hsqldb:hsqldb")
    // runtimeOnly("org.slf4j:slf4j-simple")
    runtimeOnly("org.apache.logging.log4j:log4j-core")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j2-impl")

    testImplementation("jakarta.platform:jakarta.jakartaee-api")
    // testImplementation("org.eclipse.microprofile:microprofile:6.1")
    // testImplementation("org.glassfish.jersey.core:jersey-client")
    // testImplementation("org.glassfish.jersey.media:jersey-media-json-jackson")
}

tasks.test {
    enabled = true

    testLogging {
        events("PASSED", "FAILED", "SKIPPED", "STANDARD_OUT", "STANDARD_ERROR")
        // exceptionFormat = TestExceptionFormat.FULL
    }

    systemProperty("wl_httpPort", libertyServerHttpPort)
    systemProperty("context_root", libertyContextRoot)

    dependsOn("libertyStart")
    finalizedBy("libertyStop")
}
// test.finalizedBy("openBrowser")

tasks.clean {
    dependsOn("libertyStop")
}

tasks.named<War>("war") {
    dependsOn("generateOpenApiSpec")

    // Skinny-War
    rootSpec.exclude("**/*.jar", "**/*.rar")

    // OpenApi Dateien sollten automatisch im WAR/META-INF landen.
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    metaInf {
        from(layout.buildDirectory.get().dir("generated").dir("openapi"))
    }

    // or by Filter
    // classpath = classpath.filter { file ->
    //     !file.name.startsWith("slf4j-api") && !file.name.startsWith("aws-java-sdk-simpledb")
    // }

    // archiveFileName = "sample.war"
    // webAppDirectory = file("src/main/webapp")
    // from "src/rootContent'" // adds a file-set to the root of the archive
    // webXml = file("src/someWeb.xml") // copies a file to WEB-INF/web.xml
}

tasks.named<Ear>("ear") {
    appDirectory = file("src/main/app")  // use application metadata found in this folder

    libDirName = "APP-INF/lib" // put dependent libraries into APP-INF/lib inside the generated EAR

    deploymentDescriptor {  // custom entries for application.xml:
        // fileName = "application.xml"  // same as the default value
        // version = "6"  // same as the default value
        applicationName = "custom-ear"
        initializeInOrder = true
        displayName = "Custom Ear"  // defaults to project.name
        // defaults to project.description if not set
        description = "My customized EAR for the Gradle documentation"
        // libraryDirectory = "APP-INF/lib"  // not needed, above libDirName setting does this
        // module("my.jar", "java")  // won't deploy as my.jar isn't deploy dependency
        // webModule("my.war", "/")  // won't deploy as my.war isn't deploy dependency
        securityRole("admin")
        securityRole("superadmin")

        // withXml { provider ->  // add a custom node to the XML
        //     provider.asNode().appendNode("data-source", "my/data/source")
        // }
    }
}

tasks.register("openBrowser") {
    group = "MyTasks"
    description = "Open browser to the running application"

    doLast {
        val port = libertyServerHttpPort
        val context = libertyContextRoot
        val url = "http://localhost:$port/$context/my-liberty/service/properties"

        Desktop.getDesktop().browse(URI(url))
        // Desktop.desktop.browse(file("${layout.buildDirectory.get()}/reports/tests/test/index.html").toURI())
    }
}

// configure<io.openliberty.tools.gradle.extensions.LibertyExtension> {
liberty {
    install {
        baseDir = "/tmp"
    }

    server.apply {
        // baseDir = layout.buildDirectory.dir("ibm").get()
        name = project.name

        // stripVersion(true)
        stripVersion = true

        // Clean logs, workarea, apps, dropins on server startup
        clean = true

        // Wait n seconds to verify application start.
        // verifyAppStartTimeout = 30

        // Embedded config in src/main/liberty

        // configDirectory = file("config")
        // bootstrapProperties = ["default.http.port":"9080", "default.https.port":"9443"]
        // jvmOptions = ["-Xms128m", "-Xmx512m"]
        serverXmlFile = file("config/appl/wl_config/appl.xml")
        bootstrapPropertiesFile = file("config/bootstrap.properties")
        jvmOptionsFile = file("config/jvm.options")
        serverEnvFile = file("config/server.env")
    }
}

// build/generated/openapi
configure<io.smallrye.openapi.gradleplugin.SmallryeOpenApiExtension> {
    infoTitle.set("My API's")
    infoVersion.set(project.version.toString())
    encoding.set("UTF-8")
    scanPackages.set(listOf("de.freese.liberty.rest"))
    // scanClasses = ["CLASS_1", "CLASS_2"]
    servers.set(listOf("http://localhost:9080/liberty-demo"))
}

tasks.processResources {
    finalizedBy("generateOpenApiSpec")
}

// https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-gradle-plugin
// https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/java.md
// http://localhost:9080/liberty-demo/openapi/ui/
configure<org.openapitools.generator.gradle.plugin.extensions.OpenApiGeneratorGenerateExtension> {
    generatorName.set("java")

    globalProperties.set(
        mapOf(
            // force only the models
            "apis" to "false", "invokers" to "false", "models" to ""
        )
    )

    configOptions.set(
        mapOf(
            "containerDefaultToNull" to "true", "dateLibrary" to "java8-localdatetime",
            // Eigentlich ist es hier 'microprofile', jedoch wird dann kein Jackson, sondern JsonB generiert.
            // Jsonb haben wir aber nicht im Server, weshalb die Annotation nicht erkannt werden und die Serialisierung fehlschlägt.
            // Da wir nur das Model brauchen, nehmen wir einfach 'resteasy' als Workaround.
            "library" to "resteasy", "microprofileRestClientVersion" to "3.0", "useOneOfInterfaces" to "true", "serialisationLibrary" to "jackson", "useJakartaEe" to "true"
        )
    )

    inputSpec.set(layout.buildDirectory.get().dir("generated").dir("openapi").file("openapi.json").toString())
    // inputSpec = "$rootDir/libs/openapi/openapi-pps/schema/pps-openapi.json"
    // inputSpec = "$projectDir/schema/pps-openapi.json"
    // inputSpec = "schema/pps-openapi.json"
    outputDir.set(destDir.toString())
    apiPackage.set("de.freese.liberty.api")
    modelPackage.set("de.freese.liberty.model")
    generateModelTests.set(false)
    generateModelDocumentation.set(false)
    generateApiTests.set(false)
    generateApiDocumentation.set(false)
    logToStderr.set(true)
}

tasks.named("openApiGenerate") {
    dependsOn("generateOpenApiSpec")
    mustRunAfter("generateOpenApiSpec")
}
// compileJava.finalizedBy("openApiGenerate")

tasks.register("deployApp") {
    group = "MyTasks"
    description = "Deploy Application"

    dependsOn("war")
    dependsOn("libertyCreate")

    doLast {
        val properties = Properties()
        file("config/bootstrap.properties").inputStream().use { properties.load(it) }

        val serverHome = properties.getProperty("server_home")
        println("server_home = $serverHome")
        mkdir(serverHome)

        delete("$serverHome/appl")
        delete("$serverHome/appl_data")
        delete("$serverHome/appl_logs")
        delete("$serverHome/logs")
        delete("$serverHome/tranlog")
        delete("$serverHome/workarea")
        delete("$serverHome/resources/security")
        delete("$serverHome/mqjms.log.0")

        mkdir("$serverHome/appl_data")
        mkdir("$serverHome/appl_logs")
        mkdir("$serverHome/resources/security")

        // Copy Certificates.
        copy {
            from(layout.projectDirectory.dir("config").dir("application").dir("security"))
            into("$serverHome/resources/security")
        }

        // Copy Server Config.
        copy {
            from(layout.projectDirectory.dir("config"))
            include("*.env")
            include("*.options")
            include("*.properties")
            include("*.xml")
            into(serverHome)
        }

        // Copy Appl Config.
        val applConfigFolder = "$serverHome/appl/wl_config"
        copy {
            from(layout.projectDirectory.dir("config").dir("appl").dir("wl_config"))
            include("*.properties")
            include("*.xml")
            into(applConfigFolder)
        }

        // Copy WAR.
        val warFolder = "$serverHome/appl/${properties.getProperty("deploy_folder")}"
        copy {
            from(tasks.war)
            into(warFolder)
            rename { fileName: String ->
                fileName.replace("-$version", "")
            }
        }

        // Copy Libs.
        val libFolder = "$serverHome/appl/appl_libs"
        copy {
            from({
                val runtimeFiles = configurations.runtimeClasspath.get().files
                val providedFiles = configurations.named("providedCompile").get().files

                // Nutzt Kotlin's standardmäßige Set-Subtraktion für die Files.
                runtimeFiles - providedFiles
            })
            include("*.jar")
            include("*.rar")
            //    exclude("ohj*")
            into(libFolder)
        }
    }
}

tasks.named("deploy") {
    dependsOn("deployApp")
}

// ant.lifecycleLogLevel = "INFO"
//
// // Register ANT-Tasks as Gradle-Tasks with Prefix "ant-" in Group "other".
// ant.importBuild("ant_deploy.xml") { antTargetName -> "ant-" + antTargetName }
// ant.properties["version"] = project.version
//
// // Set group property for all Ant tasks.
// tasks.matching { task -> task.name.startsWith("ant-") }.configureEach { group = "Ant" }
//
// clean.finalizedBy("ant-clean")

// tasks.register<Copy>("copyLibs") {
//     group = "MyTasks"
//
//     val properties = java.util.Properties()
//     file("config/bootstrap.properties").inputStream().use { properties.load(it) }
//
//     println("server_home = ${properties.getProperty("server_home")}")
//
//     from(configurations.runtimeClasspath - configurations.getByName("providedCompile"))
//     into("${properties.getProperty("server_home")}/appl/appl_libs")
//     include("*.jar")
//     include("*.rar")
// }
// deploy.dependsOn("copyLibs")

// tasks.register("prepareGeneratedCode") {
//     group = "star"
//     description = "Prepare the generated Code"
//
//     dependsOn("openApiGenerate")
//
//     doLast {
//         // Das System hat die Typ-Information manuell in die Objekte eingefügt.
//         // So passt das Schema nicht immer mit den Objekten zusammen.
//         // Hiermit weisen wir Jackson an die Typ-Information in DsObject.type nur zu setzen, wenn wir es explizit setzen.
//         ant.withGroovyBuilder {
//             "replaceregexp"(
//                 match = "@JsonTypeInfo\\(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = \"\", visible = true\\)\n",
//                 replace = "@JsonTypeInfo\\(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.PROPERTY, property = \"\", visible = true\\)\n"
//             ) {
//                 "fileset"(dir = destDir, includes = "**/DsObject.java")
//             }
//         }
//     }
// }

