package com.example.ucp2.ui.view.dosen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2.ui.customewidget.TopAppBar
import com.example.ucp2.ui.navigation.AlamatNavigasi
import com.example.ucp2.ui.viewmodel.DosenEvent
import com.example.ucp2.ui.viewmodel.DosenUiState
import com.example.ucp2.ui.viewmodel.DosenViewModel
import com.example.ucp2.ui.viewmodel.FormErrorStateDosen
import com.example.ucp2.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

object DestinasiInsertDosen : AlamatNavigasi {
    override val route: String = "insert_dosen"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDosen(
    dosenEvent: DosenEvent = DosenEvent(),
    onValueChange: (DosenEvent) -> Unit = {},
    errorState: FormErrorStateDosen = FormErrorStateDosen(),
    modifier: Modifier = Modifier
) {
    val jenisKelamin = listOf("Laki-laki", "Perempuan")

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Nama Dosen
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dosenEvent.nama,
            onValueChange = { onValueChange(dosenEvent.copy(nama = it)) },
            label = { Text(text = "Nama Dosen") },
            isError = errorState.nama != null,
            placeholder = { Text("Masukkan Nama Dosen") },
            shape = MaterialTheme.shapes.medium,  // Rounded corners for input fields
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        )
        Text(
            text = errorState.nama ?: "",
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall
        )

        // NIDN Dosen
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dosenEvent.nidn,
            onValueChange = { onValueChange(dosenEvent.copy(nidn = it)) },
            label = { Text("NIDN Dosen") },
            isError = errorState.nidn != null,
            placeholder = { Text("Masukkan NIDN Dosen") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        )
        Text(
            text = errorState.nidn ?: "",
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall
        )

        // Jenis Kelamin
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Jenis Kelamin", style = MaterialTheme.typography.bodyMedium)
        Row(modifier = Modifier.fillMaxWidth()) {
            jenisKelamin.forEach { jk ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    RadioButton(
                        selected = dosenEvent.jenisKelamin == jk,
                        onClick = { onValueChange(dosenEvent.copy(jenisKelamin = jk)) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = jk)
                }
            }
        }
        Text(
            text = errorState.jenisKelamin ?: "",
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun InsertBodyDosen(
    modifier: Modifier = Modifier,
    onValueChange: (DosenEvent) -> Unit,
    uiState: DosenUiState,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormDosen(
            dosenEvent = uiState.dosenEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Simpan", color = Color.White)
        }
    }
}

@Composable
fun InsertDosenView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DosenViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.resetSnackBarMessage()
            }
        }
    }
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TopAppBar(
                onBack = onBack,
                showBackButton = true,
                judul = "Tambah Dosen"
            )
            InsertBodyDosen(
                uiState = uiState,
                onValueChange = { updateEvent ->
                    viewModel.updateState(updateEvent)
                },
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveData()  // Simpan data dosen ke database
                    }
                    onNavigate()  // Navigasi setelah menyimpan
                }
            )
        }
    }
}

