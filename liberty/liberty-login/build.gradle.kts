import java.awt.Desktop
import java.net.URI

plugins {
    id("war")
    id("io.openliberty.tools.gradle.Liberty")
}

// https://openliberty.io/guides/gradle-intro.html#getting-started
description = "Demo for JSF-Login"

val libertyServerHttpPort = "7080"
val libertyServerHttpsPort = "7443"
val libertyContextRoot = name

dependencies {
    libertyRuntime("io.openliberty:openliberty-kernel:" + property("version_openliberty"))

    implementation("org.primefaces:primefaces")
    // implementation("org.primefaces:primefaces::jakarta")
    implementation("org.slf4j:slf4j-api")

    compileOnly("jakarta.platform:jakarta.jakartaee-api")

    runtimeOnly("org.primefaces:primefaces-themes")
    runtimeOnly("org.slf4j:slf4j-simple")

    // // testImplementation("org.eclipse.microprofile:microprofile:6.1")
    // testImplementation("com.fasterxml.jackson.jaxrs:jackson-jaxrs-json-provider")
    // testImplementation("org.glassfish.jersey.core:jersey-client")
    // testImplementation("org.glassfish.jersey.media:jersey-media-json-jackson")
}

tasks.test {
    enabled = true

    testLogging {
        events("PASSED", "FAILED", "SKIPPED", "STANDARD_OUT", "STANDARD_ERROR")
    }

    systemProperty("http.port", libertyServerHttpPort)
    systemProperty("context.root", libertyContextRoot)
}

tasks.register("openBrowser") {
    group = "MyTasks"
    description = "Open browser to the running application"

    doLast {
        val port = libertyServerHttpPort
        val context = libertyContextRoot
        val url = "http://localhost:" + port + "/" + context + "/login-app/service/properties"

        Desktop.getDesktop().browse(URI(url))
        // Desktop.desktop.browse(file("${layout.buildDirectory.get()}/reports/tests/test/index.html").toURI())
    }
}

tasks.test {
    dependsOn("libertyStart")
    finalizedBy("libertyStop")
    // finalizedBy("openBrowser")
}

tasks.clean {
    dependsOn("libertyStop")
}

// tasks.withType<io.openliberty.tools.gradle.tasks.DeployTask>().configureEach {
//     notCompatibleWithConfigurationCache("Das Liberty-Plugin unterstützt den Configuration Cache noch nicht.")
// }
tasks.configureEach {
    if (this::class.java.name.startsWith("io.openliberty.tools.gradle.tasks.")) {
        // println(this::class.java.name)
        notCompatibleWithConfigurationCache("Das Liberty-Plugin unterstützt den Configuration Cache noch nicht.")
    }
}
liberty {
    // install {
    //     baseDir = "/tmp"
    // }
    server.apply {
        configDirectory = file("src/main/liberty/config")
        baseDir = layout.buildDirectory.dir("ibm").get().toString()
        name = project.name

        stripVersion = true

        // Clean logs, workarea, apps, dropins on server startup
        clean = true

        // Wait n seconds to verify application start.
        // verifyAppStartTimeout = 30

        // Embedded config in src/main/liberty

        // configDirectory = file("config")
        // bootstrapProperties = ["default.http.port":"9080", "default.https.port":"9443"]
        // jvmOptions = ["-Xms128m", "-Xmx512m"]
        // serverXmlFile = file("config/appl/wl_config/appl.xml")
        // bootstrapPropertiesFile = file("config/bootstrap.properties")
        // jvmOptionsFile = file("config/jvm.options")
        // serverEnvFile = file("config/server.env")
    }
}

tasks.named<io.openliberty.tools.gradle.tasks.DevTask>("libertyDev") {
    // Debug-Port für den Java-Debugger festlegen (Standard wäre 7777).
    setLibertyDebugPort("7778")

    // debug = false // Würde den Debug-Modus komplett deaktivieren.
}