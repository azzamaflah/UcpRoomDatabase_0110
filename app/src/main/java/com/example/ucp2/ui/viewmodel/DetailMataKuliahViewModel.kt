package com.example.ucp2.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.MataKuliah
import com.example.ucp2.repository.RepositoryMatakuliah
import com.example.ucp2.ui.navigation.AlamatNavigasi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailMatakuliahViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryMataKuliah: RepositoryMatakuliah,
) : ViewModel() {
    private val _kode: String = checkNotNull(savedStateHandle[AlamatNavigasi.DestinasiDetailMataKuliah.KODE])
    val detailUiState: StateFlow<DetailMatakuliahUiState> = repositoryMataKuliah.getMatakuliah(_kode)
        .filterNotNull()
        .map {
            DetailMatakuliahUiState(
                detailUiEvent = it.toDetailMatakuliahUiEvent(),
                isLoading = false,
            )
        }
        .onStart {
            emit(DetailMatakuliahUiState(isLoading = true))
            delay(600)
        }
        .catch {
            emit(
                DetailMatakuliahUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = it.message ?: "Terjadi Kesalahan",
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = DetailMatakuliahUiState(
                isLoading = true,
            ),
        )

    fun deleteMataKuliah() {
        detailUiState.value.detailUiEvent.toMataKuliahEntity().let {
            viewModelScope.launch {
                repositoryMataKuliah.deleteMatakuliah(it)
            }
        }
    }
}

data class DetailMatakuliahUiState(
    val detailUiEvent: MataKuliahEvent = MataKuliahEvent(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
) {
    val isUiEventEmpty: Boolean
        get() = detailUiEvent == MataKuliahEvent()

    val isUiEventNotEmpty: Boolean
        get() = detailUiEvent != MataKuliahEvent()
}

fun MataKuliah.toDetailMatakuliahUiEvent(): MataKuliahEvent {
    return MataKuliahEvent(
        kode = kode,
        nama = nama,
        sks = sks,
        semester = semester,
        jenis = jenis,
        dosenPengampu = dosenpengampu
    )
}