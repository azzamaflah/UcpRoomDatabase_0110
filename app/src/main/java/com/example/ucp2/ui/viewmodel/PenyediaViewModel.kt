package com.example.ucp2.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ucp2.RunApp

object PenyediaViewModel {
    val Factory = viewModelFactory {
        // Initializer untuk DosenViewModel
        initializer {
            DosenViewModel(
                runApp().containerApp.repositoryDosen
            )
        }

        // Initializer untuk HomeDosenViewModel
        initializer {
            HomeDosenViewModel(
                runApp().containerApp.repositoryDosen
            )
        }

        // Initializer untuk MataKuliahViewModel
        initializer {
            MataKuliahViewModel(
                runApp().containerApp.repositoryMatakuliah,
                runApp().containerApp.repositoryDosen
            )
        }

        // Initializer untuk HomeMataKuliahViewModel
        initializer {
            HomeMataKuliahViewModel(
                runApp().containerApp.repositoryMatakuliah
            )
        }

        // Initializer untuk DetailMataKuliahViewModel
        initializer {
            DetailMatakuliahViewModel(
                createSavedStateHandle(),
                runApp().containerApp.repositoryMatakuliah
            )
        }

        // Initializer untuk UpdateMataKuliahViewModel
        initializer {
            UpdateMataKuliahViewModel(
                createSavedStateHandle(),
                runApp().containerApp.repositoryMatakuliah,
                runApp().containerApp.repositoryDosen
            )
        }
    }
}

// Extension function untuk mengakses instance RunApp
fun CreationExtras.runApp(): RunApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RunApp)
