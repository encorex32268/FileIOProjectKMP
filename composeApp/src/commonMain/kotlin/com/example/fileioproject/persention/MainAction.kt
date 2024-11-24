package com.example.fileioproject.persention

import io.github.vinceglb.filekit.core.PlatformFile

sealed interface MainAction {
    data class PickedData(
        val platformFile: PlatformFile?
    ): MainAction
    data object BackupData: MainAction
    data object AddNewPerson: MainAction
    data object RemoveNewPerson: MainAction
}