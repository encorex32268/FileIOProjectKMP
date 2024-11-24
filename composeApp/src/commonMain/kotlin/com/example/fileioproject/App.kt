package com.example.fileioproject

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fileioproject.persention.MainAction
import com.example.fileioproject.persention.MainViewModel
import de.halfbit.csv.buildCsv
import de.halfbit.csv.parseCsv
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import fileioproject.composeapp.generated.resources.Res
import fileioproject.composeapp.generated.resources.compose_multiplatform
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.FileKitPlatformSettings
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
@Preview
fun App(
    viewModel: MainViewModel = viewModel { MainViewModel() }
) {
    MaterialTheme {
        val state by viewModel.state.collectAsState()
        val scope = rememberCoroutineScope()
        val singleFilePicker = rememberFilePickerLauncher(
            type = PickerType.File()
        ){
            viewModel.onEvent(
                MainAction.PickedData(it)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                scope.launch{
                    viewModel.onEvent(
                        MainAction.BackupData
                    )
                }
            }) {
                Text("Save File")
            }
            Button(onClick = {
                singleFilePicker.launch()
            }) {
                Text("Pick File")
            }
            Button(onClick = {
                viewModel.onEvent(
                    MainAction.AddNewPerson
                )
            }) {
                Text("Add Person")
            }
            Button(onClick = {
                viewModel.onEvent(
                    MainAction.RemoveNewPerson
                )
            }) {
                Text("Remove Person")
            }

            LazyColumn(
                modifier = Modifier.weight(1f)
            ){
                items(
                    state.items
                ) {
                    Text("Person: ${it} / ${it.name}")
                }
            }
        }
    }
}
