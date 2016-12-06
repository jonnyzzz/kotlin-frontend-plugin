package org.jetbrains.kotlin.gradle.frontend.util

import org.gradle.api.*
import org.jetbrains.kotlin.gradle.dsl.*
import java.io.*

fun kotlinOutput(project: Project): File {
    return project.tasks.filterIsInstance<KotlinJsCompile>()
            .filter { !it.name.contains("test", ignoreCase = true) }
            .mapNotNull { it.kotlinOptions.outputFile }
            .map { project.file(it) }
            .distinct()
            .singleOrNull()
            ?.ensureParentDir()
            ?: throw GradleException("Only one kotlin output directory supported by frontend plugin.")
}

private fun File.ensureParentDir(): File = apply { parentFile.ensureDir() }

private fun File.ensureDir(): File = apply {
    if (mkdirs() && !exists()) {
        throw IOException("Failed to create directory $this")
    }
    if (!isDirectory) {
        throw IOException("Path is not a directory: $this")
    }
}