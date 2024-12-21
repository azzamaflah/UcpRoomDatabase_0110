package com.example.ucp2.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Dosen
import com.example.ucp2.data.entity.MataKuliah
import com.example.ucp2.repository.RepositoryDosen
import com.example.ucp2.repository.RepositoryMatakuliah
import com.example.ucp2.ui.navigation.AlamatNavigasi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UpdateMataKuliahViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryMataKuliah: RepositoryMatakuliah,
    private val repositoryDosen: RepositoryDosen
) : ViewModel() {

    val dosenListState: StateFlow<List<Dosen>> = repositoryDosen.getAllDosen()
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    var dosenListInsert by mutableStateOf<List<String>>(emptyList())
        private set

    fun fetchDosenList() {
        viewModelScope.launch {
            try {
                repositoryDosen.getAllDosen()
                    .collect{ dosenList->
                        dosenListInsert = dosenList.map { it.nama }
                    }
            } catch (e: Exception) {
                dosenListInsert = emptyList()
            }
        }
    }

    var updateUIState by mutableStateOf(MataKuliahUiState())
        private set

    private val _kode: String = checkNotNull(savedStateHandle[AlamatNavigasi.DestinasiDetailMataKuliah.KODE])

    init {
        viewModelScope.launch {
            updateUIState = repositoryMataKuliah.getMatakuliah(_kode)
                .filterNotNull()
                .first()
                .toMataKuliahUiState()
        }
    }

    fun updateState(mataKuliahEvent: MataKuliahEvent) {
        updateUIState = updateUIState.copy(
            mataKuliahEvent = mataKuliahEvent,
        )
    }

    fun validateFields(): Boolean {
        val event = updateUIState.mataKuliahEvent
        val errorState = FormErrorStateMataKuliah(
            kode = if (event.kode.isNotEmpty()) null else "Kode tidak boleh kosong",
            nama = if (event.nama.isNotEmpty()) null else "Nama tidak boleh kosong",
            sks = if (event.sks.isNotEmpty()) null else "SKS tidak boleh kosong",
            semester = if (event.semester.isNotEmpty()) null else "Semester tidak boleh kosong",
            jenis = if (event.jenis.isNotEmpty()) null else "Jenis tidak boleh kosong",
            dosenPengampu = if (event.dosenPengampu.isNotEmpty()) null else "Dosen Pengampu tidak boleh kosong"
        )

        updateUIState = updateUIState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    fun updateData() {
        val currentEvent = updateUIState.mataKuliahEvent

        if (validateFields()) {
            viewModelScope.launch {
                try {
                    repositoryMataKuliah.updateMatakuliah(currentEvent.toMataKuliahEntity())
                    updateUIState = updateUIState.copy(
                        snackBarMessage = "Data berhasil diupdate",
                        mataKuliahEvent = MataKuliahEvent(),
                        isEntryValid = FormErrorStateMataKuliah()
                    )
                } catch (e: Exception) {
                    updateUIState = updateUIState.copy(
                        snackBarMessage = "Data gagal diupdate"
                    )
                }
            }
        } else {
            updateUIState = updateUIState.copy(
                snackBarMessage = "Data gagal diupdate"
            )
        }
    }

    fun resetSnackBarMessage() {
        updateUIState = updateUIState.copy(snackBarMessage = null)
    }
}

fun MataKuliah.toMataKuliahUiState(): MataKuliahUiState = MataKuliahUiState(
    mataKuliahEvent = this.toDetailMatakuliahUiEvent(),
)