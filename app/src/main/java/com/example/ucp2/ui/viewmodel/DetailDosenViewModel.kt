package com.example.ucp2.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Dosen
import com.example.ucp2.repository.RepositoryDosen
import com.example.ucp2.ui.navigation.AlamatNavigasi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class DetailDosenViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryDosen: RepositoryDosen
) : ViewModel() {

    private val _nidn: String =
        checkNotNull(savedStateHandle[AlamatNavigasi.DestinasiDetailDosen.NIDN])
    val detailUiState: StateFlow<DetailDosenUiState> = repositoryDosen.getDosen(_nidn)
        .filterNotNull()
        .map {
            DetailDosenUiState(
                detailUiEvent = it.toDetailDosenUiEvent(),
                isLoading = false,
            )
        }
        .onStart {
            emit(DetailDosenUiState(isLoading = true))
            delay(600)
        }
        .catch {
            emit(
                DetailDosenUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = it.message ?: "Terjadi Kesalahan",
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = DetailDosenUiState(
                isLoading = true,
            ),
        )
    }

data class DetailDosenUiState(
    val detailUiEvent: DosenEvent = DosenEvent(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
) {
    val isUiEventEmpty: Boolean
        get() = detailUiEvent == DosenEvent()

    val isUiEventNotEmpty: Boolean
        get() = detailUiEvent != DosenEvent()
}

fun Dosen.toDetailDosenUiEvent(): DosenEvent {
    return DosenEvent(
        nidn = nidn,
        nama = nama,
        jenisKelamin = jeniskelamin
    )
}