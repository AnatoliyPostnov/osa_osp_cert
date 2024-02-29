package com.example.cert.data.utils

import java.io.File

fun createFileLocation(fileName: String): String {
    val currDir = File(".")
    val path: String = currDir.absolutePath
    return path.substring(0, path.length - 1) + fileName
}
