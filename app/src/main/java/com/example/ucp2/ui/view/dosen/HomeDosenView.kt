package com.example.ucp2.ui.view.dosen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2.data.entity.Dosen
import com.example.ucp2.ui.customewidget.TopAppBar
import com.example.ucp2.ui.viewmodel.HomeDosenState
import com.example.ucp2.ui.viewmodel.HomeDosenViewModel
import com.example.ucp2.ui.viewmodel.HomeUiState
import com.example.ucp2.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

// HomeDosenView bertanggung jawab untuk menampilkan tampilan utama daftar dosen
@Composable
fun HomeDosenView(
    viewModel: HomeDosenViewModel = viewModel(factory = PenyediaViewModel.Factory),
    onAddDosen: () -> Unit = { },
    onDetailClick: (String) -> Unit = { },
    modifier: Modifier = Modifier
) {
    // Scaffold adalah komponen layout dasar yang menyediakan struktur seperti TopAppBar dan FloatingActionButton
    Scaffold(
        topBar = {
            TopAppBar(
                onBack = { },  // Fungsi kosong untuk tombol back
                showBackButton = false,  // Tidak menampilkan tombol back
                judul = "Daftar Dosen"   // Judul tampilan
            )
        },
        floatingActionButton = {
            // FloatingActionButton yang digunakan untuk menambahkan dosen
            FloatingActionButton(
                onClick = onAddDosen,  // Fungsi untuk menambahkan dosen saat FAB diklik
                shape = MaterialTheme.shapes.medium,  // Bentuk FAB
                modifier = Modifier.padding(16.dp)  // Memberikan padding agar FAB tidak terlalu rapat dengan tepi
            ) {
                Icon(
                    imageVector = Icons.Default.Add,  // Ikon FAB berupa ikon tambah
                    contentDescription = "Tambah Dosen"
                )
            }
        }
    ) { innerPadding ->
        // Mengambil state UI dari ViewModel
        val HomeDosenState by viewModel.HomeUiState.collectAsState()

        // Body tampilan dosen yang akan menampilkan data berdasarkan status UI
        BodyHomeDosenView(
            HomeDosenState = HomeDosenState,  // Mengirimkan status UI dosen
            onClick = { onDetailClick(it) },  // Fungsi untuk mengarahkan ke detail dosen
            modifier = Modifier.padding(innerPadding)  // Padding untuk inner content
        )
    }
}

// BodyHomeDosenView menampilkan data dosen dengan berbagai kondisi UI seperti loading atau error
@Composable
fun BodyHomeDosenView(
    HomeDosenState: HomeDosenState,
    onClick: (String) -> Unit = { },
    modifier: Modifier = Modifier
) {
    // Menggunakan coroutineScope untuk menampilkan Snackbar saat ada error
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }  // Snackbar state untuk menampilkan pesan error

    // Kondisi rendering berdasarkan status UI
    when {
        HomeDosenState.isLoading -> {
            // Jika data sedang dimuat, tampilkan CircularProgressIndicator
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()  // Menampilkan indikator loading
            }
        }

        HomeDosenState.isError -> {
            // Jika terjadi error, tampilkan pesan error menggunakan Snackbar
            LaunchedEffect(HomeDosenState.errorMessage) {
                HomeDosenState.errorMessage?.let { message ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message)  // Menampilkan Snackbar dengan pesan error
                    }
                }
            }
        }

        HomeDosenState.listDosen.isEmpty() -> {
            // Jika tidak ada data dosen, tampilkan teks yang memberi tahu pengguna
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tidak ada data dosen.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        else -> {
            // Jika data dosen ada, tampilkan daftar dosen
            ListDosen(
                listDosen = HomeDosenState.listDosen,
                onClick = { onClick(it) },
                modifier = modifier
            )
        }
    }
}

// ListDosen menampilkan daftar dosen dalam format LazyColumn yang efisien
@Composable
fun ListDosen(
    listDosen: List<Dosen>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = { }
) {
    // LazyColumn untuk menampilkan daftar dosen dengan scroll yang efisien
    LazyColumn(
        modifier = modifier
    ) {
        // Mengiterasi listDosen dan menampilkan setiap item dalam CardDosen
        items(items = listDosen) { dosen ->
            CardDosen(
                dosen = dosen,
                onClick = { onClick(dosen.nidn) }  // Fungsi yang akan dijalankan saat Card diklik
            )
        }
    }
}

// CardDosen adalah kartu untuk setiap dosen yang menampilkan informasi dosen dalam bentuk baris
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDosen(
    dosen: Dosen,
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
            // Row pertama menampilkan nama dosen
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "")  // Ikon untuk nama
                Spacer(modifier = Modifier.padding(5.dp))  // Memberikan ruang antar ikon dan teks
                Text(
                    text = dosen.nama,  // Nama dosen
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Row kedua menampilkan NIDN dosen
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.AccountBox, contentDescription = "")  // Ikon untuk NIDN
                Spacer(modifier = Modifier.padding(5.dp))  // Memberikan ruang antar ikon dan teks
                Text(
                    text = dosen.nidn,  // NIDN dosen
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Row ketiga menampilkan jenis kelamin dosen
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Face, contentDescription = "")  // Ikon untuk jenis kelamin
                Spacer(modifier = Modifier.padding(5.dp))  // Memberikan ruang antar ikon dan teks
                Text(
                    text = dosen.jeniskelamin,  // Jenis kelamin dosen
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
