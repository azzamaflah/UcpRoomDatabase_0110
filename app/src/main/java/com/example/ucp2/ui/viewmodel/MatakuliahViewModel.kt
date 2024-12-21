package com.example.ucp2.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Dosen
import com.example.ucp2.data.entity.MataKuliah
import com.example.ucp2.repository.RepositoryDosen
import com.example.ucp2.repository.RepositoryMatakuliah
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MataKuliahViewModel(private val repositoryMatakuliah: RepositoryMatakuliah, private val RepositoryDosen: RepositoryDosen) : ViewModel() {
    var uiState by mutableStateOf(MataKuliahUiState())

    val dosenListState: StateFlow<List<Dosen>> = RepositoryDosen.getAllDosen()
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
                RepositoryDosen.getAllDosen()
                    .collect{ dosenList->
                        dosenListInsert = dosenList.map { it.nama }
                 }
            } catch (e: Exception) {
            dosenListInsert = emptyList()
            }
        }
    }

    fun updateState(mataKuliahEvent: MataKuliahEvent) {
        uiState = uiState.copy(
            mataKuliahEvent = mataKuliahEvent,
        )
    }

    private fun validateFields(): Boolean {
        val event = uiState.mataKuliahEvent
        val errorState = FormErrorStateMataKuliah(
            kode = if (event.kode.isNotEmpty()) null else "Kode tidak boleh kosong",
            nama = if (event.nama.isNotEmpty()) null else "Nama tidak boleh kosong",
            sks = if (event.sks.isNotEmpty()) null else "SKS tidak boleh kosong",
            semester = if (event.semester.isNotEmpty()) null else "Semester tidak boleh kosong",
            jenis = if (event.jenis.isNotEmpty()) null else "Jenis tidak boleh kosong",
            dosenPengampu = if (event.dosenPengampu.isNotEmpty()) null else "Dosen Pengampu tidak boleh kosong"
        )
        uiState = uiState.copy(
            isEntryValid = errorState
        )
        return errorState.isValid()
    }

    fun saveData() {
        val currentEvent = uiState.mataKuliahEvent
        if (validateFields()) {
            viewModelScope.launch {
                try {
                    repositoryMatakuliah.insertMatakuliah(currentEvent.toMataKuliahEntity())
                    uiState = uiState.copy(
                        snackBarMessage = "Data berhasil disimpan",
                        mataKuliahEvent = MataKuliahEvent(),
                        isEntryValid = FormErrorStateMataKuliah()
                    )
                } catch (e: Exception) {
                    uiState = uiState.copy(
                        snackBarMessage = "Data gagal disimpan"
                    )
                }
            }
        } else {
            uiState = uiState.copy(
                snackBarMessage = "Input tidak valid, periksa kembali data Anda"
            )
        }
    }

    fun resetSnackBarMessage() {
        uiState = uiState.copy(snackBarMessage = null)
    }
}

// UI State and Event Classes for MataKuliah
data class MataKuliahUiState(
    val mataKuliahEvent: MataKuliahEvent = MataKuliahEvent(),
    val isEntryValid: FormErrorStateMataKuliah = FormErrorStateMataKuliah(),
    val snackBarMessage: String? = null,
)

data class FormErrorStateMataKuliah(
    val kode: String? = null,
    val nama: String? = null,
    val sks: String? = null,
    val semester: String? = null,
    val jenis: String? = null,
    val dosenPengampu: String? = null,
) {
    fun isValid(): Boolean {
        return kode == null && nama == null && sks == null && semester == null && jenis == null && dosenPengampu == null
    }
}

data class MataKuliahEvent(
    val kode: String = "",
    val nama: String = "",
    val sks: String = "",
    val semester: String = "",
    val jenis: String = "",
    val dosenPengampu: String = "",
)

fun MataKuliahEvent.toMataKuliahEntity(): MataKuliah = MataKuliah(
    kode = kode,
    nama = nama,
    sks = sks,
    semester = semester,
    jenis = jenis,
    dosenpengampu = dosenPengampu
)
