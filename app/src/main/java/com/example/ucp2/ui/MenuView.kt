package com.example.ucp2.ui.view.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ucp2.R
import com.example.ucp2.ui.customewidget.TopAppBar

@Composable
fun MenuView(
    onNavigateToDosen: () -> Unit,
    onNavigateToMatakuliah: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                onBack = {}, // Tidak ada tombol kembali di MenuView
                showBackButton = false,
                judul = "Selamat Datang di Menu Utama"
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center, // Tengah-tengah
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Image
            Image(
                painter = painterResource(id = R.drawable.ipul), // Pastikan logo ini ada di resources
                contentDescription = "Banner Menu",
                contentScale = ContentScale.Fit, // Menyesuaikan ukuran gambar agar pas dengan lebar
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp) // Atur tinggi gambar untuk menyesuaikan ukuran
                    .padding(bottom = 24.dp) // Mengatur jarak bawah dari gambar ke elemen lainnya
            )

            // Title
            Text(
                text = "Pilih Menu yang Ingin Diakses",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp) // Jarak yang lebih besar agar lebih jelas
            )

            // Dosen Card
            CardMenuItem(
                title = "Dosen",
                description = "Kelola data dosen yang ada di sistem.",
                icon = Icons.Default.Person,
                backgroundColor = Color(0xFF4CAF50), // Green
                onClick = onNavigateToDosen
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mata Kuliah Card
            CardMenuItem(
                title = "Mata Kuliah",
                description = "Kelola data mata kuliah dengan mudah.",
                icon = Icons.Default.Menu,
                backgroundColor = Color(0xFF03A9F4), // Blue
                onClick = onNavigateToMatakuliah
            )
        }
    }
}

@Composable
fun CardMenuItem(
    title: String,
    description: String,
    icon: ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 16.dp) // Menambahkan padding horizontal
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Section
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "$title Icon",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}
