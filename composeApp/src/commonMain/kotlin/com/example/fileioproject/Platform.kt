package com.example.fileioproject

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getDownloadsPath(): String
