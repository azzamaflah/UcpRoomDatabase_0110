package com.example.ucp2.ui.navigation

interface AlamatNavigasi {
    val route: String

    // Rute untuk Menu Utama
    object DestinasiMenu : AlamatNavigasi {
        override val route = "menu"
    }

    // Rute untuk Home Dosen
    object DestinasiHomeDosen : AlamatNavigasi {
        override val route = "home_dosen"
    }

    // Rute untuk Insert Dosen
    object DestinasiInsertDosen : AlamatNavigasi {
        override val route = "insert_dosen"
    }

    // Rute untuk Home Mata Kuliah
    object DestinasiHomeMatakuliah : AlamatNavigasi {
        override val route = "home_matakuliah"
    }

    // Rute untuk Insert Mata Kuliah
    object DestinasiInsertMatakuliah : AlamatNavigasi {
        override val route = "insert_matakuliah"
    }

    // Rute untuk Detail Dosen (menggunakan NIDN sebagai argumen)
    object DestinasiDetailDosen : AlamatNavigasi {
        override val route = "detail_dosen"
        const val NIDN = "nidn"
        val routesWithArg = "$route/{$NIDN}"
    }

    // Rute untuk Detail Mata Kuliah (menggunakan Kode sebagai argumen)
    object DestinasiDetailMataKuliah : AlamatNavigasi {
        override val route = "detail_matakuliah"
        const val KODE = "kode"
        val routesWithArg = "$route/{$KODE}"
    }

        // Rute untuk Update Mata Kuliah (menggunakan Kode sebagai argumen)
        object DestinasiUpdateMatakuliah : AlamatNavigasi {
            override val route = "update_matakuliah"
            const val KODE = "kode"
            val routesWithArg = "$route/{$KODE}"
        }
    }
