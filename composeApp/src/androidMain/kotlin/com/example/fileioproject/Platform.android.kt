package com.example.fileioproject

import android.os.Build
import android.os.Environment
import java.io.File

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun getDownloadsPath(): String {
    val downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
    val folderName = "MoneyManagerBackup"
    val newFolder = File(downloadPath , folderName)
    if (!newFolder.exists()){
        newFolder.mkdir()
    }
    return "$downloadPath/$folderName"
}