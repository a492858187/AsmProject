repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}

gradlePlugin {
    plugins {
        create("ViewClickPlugin") {
            id = "github.leavesczy.track.click.view"
            implementationClass = "github.leavesczy.track.plugins.click.view.ViewClickPlugin"
        }
    }
}

dependencies {
    implementation("androidx.privacysandbox.tools:tools-core:1.0.0-alpha06")
    val agpVersion = "8.0.2"
    val kotlinVersion = "1.8.22"
    implementation("com.android.tools.build:gradle:${agpVersion}")
    implementation("com.android.tools.build:gradle-api:${agpVersion}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:${kotlinVersion}")
    implementation("org.ow2.asm:asm-commons:9.5")
    implementation("commons-io:commons-io:2.13.0")
}