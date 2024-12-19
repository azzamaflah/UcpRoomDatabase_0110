package com.example.ucp2.ui.navigation

interface AlamatNavigasi {
    val route: String

    // Rute untuk Menu
    object DestinasiMenu : AlamatNavigasi {
        override val route = "menu"
    }

    // Rute untuk Dosen
    object DestinasiDosen : AlamatNavigasi {
        override val route = "dosen"
    }

    // Rute untuk Mata Kuliah
    object DestinasiMataKuliah : AlamatNavigasi {
        override val route = "mata_kuliah"
    }

    // Rute untuk detail dosen, menggunakan NIDN sebagai argumen
    object DestinasiDetailDosen : AlamatNavigasi {
        override val route = "detail_dosen"
        const val NIDN = "nidn"
        val routesWithArg = "$route/{$NIDN}"
    }

    // Rute untuk detail mata kuliah, menggunakan Kode sebagai argumen
    object DestinasiDetailMataKuliah : AlamatNavigasi {
        override val route = "detail_mata_kuliah"
        const val KODE = "kode"
        val routesWithArg = "$route/{$KODE}"
    }
}