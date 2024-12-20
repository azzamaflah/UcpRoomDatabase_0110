package com.example.ucp2.ui.matakuliah.view

import androidx.compose.foundation.clickable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2.ui.customewidget.TopAppBar
import com.example.ucp2.ui.navigation.AlamatNavigasi
import com.example.ucp2.ui.viewmodel.FormErrorStateMataKuliah
import com.example.ucp2.ui.viewmodel.MataKuliahEvent
import com.example.ucp2.ui.viewmodel.MataKuliahUiState
import com.example.ucp2.ui.viewmodel.MataKuliahViewModel
import com.example.ucp2.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

object DestinasiInsertMataKuliah : AlamatNavigasi {
    override val route: String = "insert_mataKuliah"
}


@Composable
fun InsertMataKuliahView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MataKuliahViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
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
    ) {padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TopAppBar(
                onBack = onBack,
                showBackButton = true,
                judul = "Tambah Mahasiswa",
            )
            InsertMataKuliah(
                uiState = uiState,
                onValueChange = {updateEvent ->
                    viewModel.updateState(updateEvent)
                },
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveData()
                    }
                    onNavigate()
                }
            )
        }
    }
}


@Composable
fun InsertMataKuliah(
    modifier: Modifier = Modifier,
    onValueChange: (MataKuliahEvent) -> Unit,
    uiState: MataKuliahUiState,
    onClick:() -> Unit
){
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormMataKuliah(
            mataKuliahEvent = uiState.mataKuliahEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Simpan")
        }
    }
}


@Composable
fun FormMataKuliah(
    mataKuliahEvent: MataKuliahEvent = MataKuliahEvent(),
    onValueChange:(MataKuliahEvent) -> Unit = {},
    errorState: FormErrorStateMataKuliah = FormErrorStateMataKuliah(),
    listDosen: List<String> = listOf("Dr. Budi", "Prof. Siti", "Dr. Ahmad"),
    modifier: Modifier = Modifier
){
    val jenis =  listOf("Wajib", "Pilihan")
    var expanded by remember { mutableStateOf(false) }
    var selectedDosen by remember { mutableStateOf(mataKuliahEvent.dosenPengampu)}

    Column(modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mataKuliahEvent.kode,
            onValueChange = {
                onValueChange(mataKuliahEvent.copy(kode = it))
            },
            label = { Text("Kode Mata Kuliah") },
            isError = errorState.kode != null,
            placeholder = { Text("Masukkan kode mata kuliah") },
        )
        Text(
            text = errorState.kode ?: "",
            color = Color.Red
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mataKuliahEvent.nama,
            onValueChange = {
                onValueChange(mataKuliahEvent.copy(nama = it))
            },
            label = { Text("Nama Mata Kuliah") },
            isError = errorState.nama != null,
            placeholder = { Text("Masukkan nama mata kuliah") },
        )
        Text(
            text = errorState.nama ?: "",
            color = Color.Red
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mataKuliahEvent.sks,
            onValueChange = {
                onValueChange(mataKuliahEvent.copy(sks = it))
            },
            label = { Text("SKS") },
            isError = errorState.sks != null,
            placeholder = { Text("Masukkan jumlah SKS") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(
            text = errorState.sks ?: "",
            color = Color.Red
        )


        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mataKuliahEvent.semester,
            onValueChange = {
                onValueChange(mataKuliahEvent.copy(semester = it))
            },
            label = { Text("Semester") },
            isError = errorState.semester != null,
            placeholder = { Text("Masukkan semester") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(
            text = errorState.semester ?: "",
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Jenis Mata Kuliah")
        Row(modifier = Modifier.fillMaxWidth()) {
            jenis.forEach { jenis ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    RadioButton(
                        selected = mataKuliahEvent.jenis == jenis,
                        onClick = {
                            onValueChange(mataKuliahEvent.copy(jenis = jenis))
                        },
                    )
                    Text(text = jenis)
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Dosen Pengampu")

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedDosen,
                onValueChange = {},
                label = { Text("Dosen Pengampu") },
                isError = errorState.dosenPengampu != null,
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                }
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            listDosen.forEach { dosen ->
                DropdownMenuItem(
                    text = { Text(text = dosen) },
                    onClick = {
                        selectedDosen = dosen
                        onValueChange(mataKuliahEvent.copy(dosenPengampu = dosen))
                        expanded = false
                    }
                )
            }
        }

        Text(
            text = errorState.dosenPengampu ?: "",
            color = Color.Red
        )
    }
}