plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "com.furkanoffice"
version = "1.0.0"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("com.furkanoffice.clock")
    mainClass.set("com.furkanoffice.clock.HelloApplication")
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("org.openjfx:javafx-controls:21.0.5")
    implementation("org.openjfx:javafx-fxml:21.0.5")
    
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "Clock"
    }

    jpackage {
        imageName = "Clock"
        installerName = "Clock"
        appVersion = "1.0.0"
        vendor = "Furkan Office Software"
        description = "Modern Kronometre ve Saat Uygulaması"

        installerOptions = listOf(
            "--win-dir-chooser",
            "--win-menu",
            "--win-shortcut"
        )
    }
}