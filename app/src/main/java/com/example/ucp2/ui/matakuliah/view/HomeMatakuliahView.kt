package com.example.ucp2.ui.matakuliah.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2.data.entity.MataKuliah
import com.example.ucp2.ui.customewidget.TopAppBar
import com.example.ucp2.ui.viewmodel.HomeMataKuliahViewModel
import com.example.ucp2.ui.viewmodel.HomeUiState
import com.example.ucp2.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

// HomeMatakuliahView bertanggung jawab untuk menampilkan tampilan utama daftar mata kuliah
@Composable
fun HomeMatakuliahView(
    viewModel: HomeMataKuliahViewModel = viewModel(factory = PenyediaViewModel.Factory),
    onAddMataKuliah: () -> Unit = { },
    onDetailClick: (String) -> Unit = { },
    modifier: Modifier = Modifier
) {
    // Scaffold adalah komponen layout dasar yang menyediakan struktur seperti TopAppBar dan FloatingActionButton
    Scaffold(
        topBar = {
            TopAppBar(
                onBack = { },  // Fungsi kosong untuk tombol back
                showBackButton = false,  // Tidak menampilkan tombol back
                judul = "Daftar Mata Kuliah"   // Judul tampilan
            )
        },
        floatingActionButton = {
            // FloatingActionButton yang digunakan untuk menambahkan mata kuliah
            FloatingActionButton(
                onClick = onAddMataKuliah,  // Fungsi untuk menambahkan mata kuliah saat FAB diklik
                shape = MaterialTheme.shapes.medium,  // Bentuk FAB
                modifier = Modifier.padding(16.dp)  // Memberikan padding agar FAB tidak terlalu rapat dengan tepi
            ) {
                Icon(
                    imageVector = Icons.Default.Add,  // Ikon FAB berupa ikon tambah
                    contentDescription = "Tambah Mata Kuliah"
                )
            }
        }
    ) { innerPadding ->
        // Mengambil state UI dari ViewModel
        val homeMataKuliahState by viewModel.HomeUiState.collectAsState()

        // Body tampilan mata kuliah yang akan menampilkan data berdasarkan status UI
        BodyHomeMatakuliahView(
            homeMataKuliahState = homeMataKuliahState,  // Mengirimkan status UI mata kuliah
            onClick = { onDetailClick(it) },  // Fungsi untuk mengarahkan ke detail mata kuliah
            modifier = Modifier.padding(innerPadding)  // Padding untuk inner content
        )
    }
}

// BodyHomeMatakuliahView menampilkan data mata kuliah dengan berbagai kondisi UI seperti loading atau error
@Composable
fun BodyHomeMatakuliahView(
    homeMataKuliahState: HomeUiState,
    onClick: (String) -> Unit = { },
    modifier: Modifier = Modifier
) {
    // Menggunakan coroutineScope untuk menampilkan Snackbar saat ada error
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }  // Snackbar state untuk menampilkan pesan error

    // Kondisi rendering berdasarkan status UI
    when {
        homeMataKuliahState.isLoading -> {
            // Jika data sedang dimuat, tampilkan CircularProgressIndicator
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()  // Menampilkan indikator loading
            }
        }

        homeMataKuliahState.isError -> {
            // Jika terjadi error, tampilkan pesan error menggunakan Snackbar
            LaunchedEffect(homeMataKuliahState.errorMessage) {
                homeMataKuliahState.errorMessage?.let { message ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message)  // Menampilkan Snackbar dengan pesan error
                    }
                }
            }
        }

        homeMataKuliahState.listMataKuliah.isEmpty() -> {
            // Jika tidak ada data mata kuliah, tampilkan teks yang memberi tahu pengguna
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tidak ada data mata kuliah.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        else -> {
            // Jika data mata kuliah ada, tampilkan daftar mata kuliah
            ListMataKuliah(
                listMataKuliah = homeMataKuliahState.listMataKuliah,
                onClick = { onClick(it) },
                modifier = modifier
            )
        }
    }
}

// ListMataKuliah menampilkan daftar mata kuliah dalam format LazyColumn yang efisien
@Composable
fun ListMataKuliah(
    listMataKuliah: List<MataKuliah>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = { }
) {
    // LazyColumn untuk menampilkan daftar mata kuliah dengan scroll yang efisien
    LazyColumn(
        modifier = modifier
    ) {
        // Mengiterasi listMataKuliah dan menampilkan setiap item dalam CardMataKuliah
        items(items = listMataKuliah) { mataKuliah ->
            CardMataKuliah(
                mataKuliah = mataKuliah,
                onClick = { onClick(mataKuliah.kode) }  // Fungsi yang akan dijalankan saat Card diklik
            )
        }
    }
}

// CardMataKuliah adalah kartu untuk setiap mata kuliah yang menampilkan informasi mata kuliah dalam bentuk baris
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardMataKuliah(
    mataKuliah: MataKuliah,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    // Menampilkan Card yang akan menerima aksi klik
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()  // Card mengisi lebar penuh
            .padding(10.dp)  // Padding untuk memberi ruang di sekitar card
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Row pertama menampilkan nama mata kuliah
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "")  // Ikon untuk nama
                Spacer(modifier = Modifier.padding(5.dp))  // Memberikan ruang antar ikon dan teks
                Text(
                    text = mataKuliah.nama,  // Nama mata kuliah
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Row kedua menampilkan kode mata kuliah
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.AccountBox, contentDescription = "")  // Ikon untuk kode mata kuliah
                Spacer(modifier = Modifier.padding(5.dp))  // Memberikan ruang antar ikon dan teks
                Text(
                    text = mataKuliah.kode,  // Kode mata kuliah
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Row ketiga menampilkan SKS mata kuliah
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Face, contentDescription = "")  // Ikon untuk SKS
                Spacer(modifier = Modifier.padding(5.dp))  // Memberikan ruang antar ikon dan teks
                Text(
                    text = "SKS: ${mataKuliah.sks}",  // SKS mata kuliah
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}