package org.tq.track

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.tq.track.configure.appModule
import org.tq.track.configure.libraryModule
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope

class TrackPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val projectName = project.name
        val isAppProject = projectName == "app"
        if (isAppProject) {
            project.apply {
                plugin("com.android.application")
                plugin("org.jetbrains.kotlin.android")
            }
        } else {
            project.apply {
                plugin("com.android.library")
                plugin("org.jetbrains.kotlin.android")
            }
        }
        println("isAppProject: " + isAppProject)
        DependencyHandlerScope.of(project.dependencies).apply {
            implementationTest()
        }
        when (val androidExtension = project.extensions.getByName("android")) {
            is BaseAppModuleExtension -> {
                androidExtension.appModule(project = project)
            }

            is LibraryExtension -> {
                androidExtension.libraryModule()
            }
        }
    }

}