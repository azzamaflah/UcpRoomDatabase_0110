package com.example.ucp2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ucp2.ui.matakuliah.view.DetailMatakuliahView
import com.example.ucp2.ui.matakuliah.view.HomeMatakuliahView
import com.example.ucp2.ui.matakuliah.view.InsertMataKuliahView
import com.example.ucp2.ui.matakuliah.view.UpdateMatakuliahView
import com.example.ucp2.ui.view.dosen.HomeDosenView
import com.example.ucp2.ui.view.dosen.InsertDosenView
import com.example.ucp2.ui.view.menu.MenuView

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = AlamatNavigasi.DestinasiMenu.route) {

        // Menu Utama
        composable(route = AlamatNavigasi.DestinasiMenu.route) {
            MenuView(
                onNavigateToDosen = {
                    navController.navigate(AlamatNavigasi.DestinasiHomeDosen.route)
                },
                onNavigateToMatakuliah = {
                    navController.navigate(AlamatNavigasi.DestinasiHomeMatakuliah.route)
                },
                modifier = modifier
            )
        }

        // Home Dosen
        composable(route = AlamatNavigasi.DestinasiHomeDosen.route) {
            HomeDosenView(
                onDetailClick = { nidn ->
                    navController.navigate("${AlamatNavigasi.DestinasiDetailDosen.route}/$nidn")
                },
                onAddDosen = {
                    navController.navigate(AlamatNavigasi.DestinasiInsertDosen.route)
                },
                modifier = modifier
            )
        }

        // Insert Dosen
        composable(route = AlamatNavigasi.DestinasiInsertDosen.route) {
            InsertDosenView(
                onBack = {
                    navController.popBackStack()
                },
                onNavigate = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }

        // Home Mata Kuliah
        composable(route = AlamatNavigasi.DestinasiHomeMatakuliah.route) {
            HomeMatakuliahView(
                onDetailClick = { kode ->
                    navController.navigate("${AlamatNavigasi.DestinasiDetailMataKuliah.route}/$kode")
                    println(
                        "pengelola halaman : kode = $kode"
                    )
                },
                onAddMataKuliah = {
                    navController.navigate(AlamatNavigasi.DestinasiInsertMatakuliah.route)
                },
                modifier = modifier
            )
        }

        // Insert Mata Kuliah
        composable(route = AlamatNavigasi.DestinasiInsertMatakuliah.route) {
            InsertMataKuliahView(
                onBack = {
                    navController.popBackStack()
                },
                onNavigate = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }

        // Detail Mata Kuliah
        composable(
            route = AlamatNavigasi.DestinasiDetailMataKuliah.routesWithArg,
            arguments = listOf(navArgument(AlamatNavigasi.DestinasiDetailMataKuliah.KODE) { type = NavType.StringType })
        ) {
            val kode = it.arguments?.getString(AlamatNavigasi.DestinasiDetailMataKuliah.KODE)
            kode?.let {
                DetailMatakuliahView(
                    onBack = {
                        navController.popBackStack()
                    },
                    onEditClick = {
                        navController.navigate("${AlamatNavigasi.DestinasiUpdateMatakuliah.route}/$it")
                    },
                    modifier = modifier
                )
            }
        }

        // Update Mata Kuliah
        composable(
            route = AlamatNavigasi.DestinasiUpdateMatakuliah.routesWithArg,
            arguments = listOf(navArgument(AlamatNavigasi.DestinasiUpdateMatakuliah.KODE) { type = NavType.StringType })
        ) {
            UpdateMatakuliahView(
                onBack = {
                    navController.popBackStack()
                },
                onNavigate = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }
    }
}
