import java.util.*

plugins {
    id("war")
    id("io.openliberty.tools.gradle.Liberty")
}

// https://openliberty.io/guides/gradle-intro.html#getting-started
description = "Demo for Spring-Integration"

val libertyServerHttpPort = "9081"
val libertyServerHttpsPort = "9444"
val libertyContextRoot = project.name

// configurations {
//     jdbcLib {
//         canBeConsumed = true
//         canBeResolved = true // false = No Dependencies
//
//         extendsFrom(implementation)
//         transitive(true)
//     }
// }

dependencies {
//    libertyRuntime("io.openliberty:openliberty-kernel:" + property("version_openliberty"))
    // libertyRuntime("io.openliberty:openliberty-runtime:" + property("version_openliberty"))

    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("com.zaxxer:HikariCP")

    // jdbcLib("com.h2database:h2")
    runtimeOnly("com.h2database:h2")

    // providedCompile
    compileOnly("jakarta.platform:jakarta.jakartaee-api")
}

// tasks.register("copyDependenciesJdbc", Sync) {
//     group = "MyTasks"
//
//     from(configurations.jdbcLib)
//     into(layout.buildDirectory.get().dir("wlp").dir("usr").dir("servers").dir(project.name).dir("libs"))
//     include("*.jar")
// }
// deploy.dependsOn("copyDependenciesJdbc")

tasks.register<Sync>("copyDependencies") {
    group = "MyTasks"

    from(configurations.runtimeClasspath.get() - configurations.providedCompile.get())
    into(layout.buildDirectory.get().dir("wlp").dir("usr").dir("servers").dir(project.name).dir("libs"))
    include("h2-**.jar")
    // include("*.jar")
    // include("*.rar")
}
tasks.named("deploy").configure {
    dependsOn("copyDependencies")
}

tasks.withType<Test>().configureEach {
    isEnabled = true

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
        val url = "http://localhost:$libertyServerHttpPort/$libertyContextRoot/sysdate"

//        if (java.awt.Desktop.isDesktopSupported()) {
//            val desktop = java.awt.Desktop.getDesktop()
//            desktop.browse(java.net.URI.create(url))
//            // desktop.browse(file("${layout.buildDirectory.get()}/reports/tests/test/index.html").toURI())
//        }
        val os = System.getProperty("os.name").lowercase()
        val processBuilder = when {
            os.contains("win") -> ProcessBuilder("cmd", "/c", "start", url)
            os.contains("mac") -> ProcessBuilder("open", url)
            else -> ProcessBuilder("xdg-open", url) // Linux (Ubuntu, Debian, etc.)
        }
        processBuilder.start()
    }
}

tasks.register("sleep") {
    group = "MyTasks"
    description = "Sleeps some seconds"

    doLast {
        logger.lifecycle("sleeping: {} seconds", 5)
        Thread.sleep(5_000L)
    }
}

tasks.named("test").configure {
    dependsOn("libertyStart")
    // finalizedBy("libertyStop")
    // finalizedBy("sleep")
    // finalizedBy("openBrowser")
}
tasks.named("test").get().dependsOn("libertyStop")

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
    server.apply {
        configDirectory = file("src/main/liberty/config")
        baseDir = layout.buildDirectory.dir("ibm").get().toString()
        name = project.name

        stripVersion = true

        // Clean logs, workarea, apps, dropins on server startup
        clean = true

        // Embedded config in src/main/liberty

        // configDirectory = file("config")
        // bootstrapProperties = ["default.http.port":"9080", "default.https.port":"9443"]
        // jvmOptions = ["-Xms128m", "-Xmx512m"]
        // serverXmlFile = file("config/appl/wl_config/appl.xml")
        // bootstrapPropertiesFile = file("config/bootstrap.properties")
        // jvmOptionsFile = file("config/jvm.options")
        // serverEnvFile = file("config/server.env")

        bootstrapProperties = Properties().apply {
            put("http.port", libertyServerHttpPort)
            put("https.port", libertyServerHttpsPort)
        }
    }
}

tasks.named<io.openliberty.tools.gradle.tasks.DevTask>("libertyDev") {
    // Debug-Port für den Java-Debugger festlegen (Standard wäre 7777).
    setLibertyDebugPort("7779")

    // debug = false // Würde den Debug-Modus komplett deaktivieren.
}

tasks.named<ProcessResources>("processResources") {
    val map = mapOf(
        "project_description" to project.description, "project_artifactId" to project.name, "project_version" to project.version.toString()
    )

    filesMatching(listOf("application.properties", "application.yml")) {
        filter(
            mapOf("tokens" to map), org.apache.tools.ant.filters.ReplaceTokens::class.java
        )
    }
}
