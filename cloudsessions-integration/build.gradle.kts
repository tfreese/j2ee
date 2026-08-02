plugins {
    id("java")
}

description = "Test for Integration."

val cloudsessionsLibs = configurations.create("cloudsessionsLibs") {
//    extendsFrom(configurations.named(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME).get())
    extendsFrom(configurations.runtimeClasspath.get())
}

val cloudsessionsWar = configurations.create("cloudsessionsWar") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    cloudsessionsLibs(project(":cloudsessions"))
    cloudsessionsWar(project(path = ":cloudsessions", configuration = "cloudsessionsWar"))
}

tasks.withType<Jar>().configureEach {
    isEnabled = false
}

tasks.withType<Test>().configureEach {
    isEnabled = false
}

tasks.register<Copy>("copyLibs") {
    group = "MyTasks"
    description = "Copies the libs from the cloudsessions based on the server."

    dependsOn(":cloudsessions:build")

    delete(layout.buildDirectory)
    into(layout.buildDirectory)

//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    into("libs") {
        from(cloudsessionsLibs) {
            exclude("cloudsessions*.jar")

            // Required, if "providedCompile" is used and not "compileOnly" in the war-Projekt.
            // exclude(group: "PROVIDED-DEPENDENCY")
        }
    }

    into("war") {
        from(cloudsessionsWar)
        rename("cloudsessions-" + project(":cloudsessions").version + ".war", "cloudsessions.war")
    }
}
tasks.named("build").get().finalizedBy("copyLibs")
