package com.example.fileioproject.persention

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fileioproject.getDownloadsPath
import com.example.fileioproject.model.Person
import de.halfbit.csv.buildCsv
import de.halfbit.csv.parseCsv
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun onEvent(
        event: MainAction
    ){
        when(event){
            MainAction.BackupData -> {
                if (state.value.items.isEmpty()) return
                if (state.value.isSaving) return
                if (state.value.isLoading) return
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isSaving = true
                        )
                    }
                    FileKit.saveFile(
                        baseName = "myBackUp",
                        extension = "txt",
                        initialDirectory = getDownloadsPath(),
                        bytes = backupData(
                            personList = state.value.items
                        )
                    )
                    delay(500L)
                    _state.update {
                        it.copy(
                            isSaving = false
                        )
                    }
                }
            }
            is MainAction.PickedData -> {
                if (event.platformFile == null) return
                readData(event.platformFile)

            }
            MainAction.AddNewPerson -> {
                val size = state.value.items.size
                _state.update {
                    it.copy(
                        items = state.value.items + Person(id = size , name = "Persion${size}")
                    )
                }
            }
            MainAction.RemoveNewPerson -> {
                _state.update {
                    it.copy(
                        items = state.value.items - state.value.items.last()
                    )
                }
            }
        }

    }

    private fun readData(
        platformFile: PlatformFile
    ){
       viewModelScope.launch {
           _state.update {
               it.copy(
                   isLoading = true
               )
           }
           val jsonString = platformFile.readBytes().decodeToString()
           val csv = parseCsv(jsonString)
           val data = csv.data.map {
               val id = it.value("id")?.toIntOrNull()?:-1
               val name = it.value("name")?:""
               Person(
                   id = id,
                   name = name
               )
           }
           val currentData = state.value.items.toMutableSet()
           currentData.addAll(data)
           _state.update {
               it.copy(
                   items = currentData.toList(),
                   isLoading = false
               )
           }
       }
    }

    private fun backupData(
        personList: List<Person> = emptyList()
    ): ByteArray{
        val csv = buildCsv {
            row {
                value("id")
                value("name")
            }
            personList.forEach {
                row {
                    value(it.id.toString())
                    value(it.name)
                }
            }
        }
        return csv.toCsvText().encodeToByteArray()
    }

}

