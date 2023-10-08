android {
    namespace = "org.tq.track"
}

plugins {
}

dependencies {
    implementation(Dependencies.Components.appcompat)
    implementation(Dependencies.Components.material)
    implementation(project(":click"))
    implementation(project(":privacy"))
    implementation(project(":thread"))
    implementation(project(":bitmap"))
}