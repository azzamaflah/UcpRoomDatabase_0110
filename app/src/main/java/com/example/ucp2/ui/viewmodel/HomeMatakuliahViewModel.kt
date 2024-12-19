package com.example.ucp2.ui.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.MataKuliah
import com.example.ucp2.repository.LocalRepositoryDosen
import com.example.ucp2.repository.RepositoryMatakuliah
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class HomeMataKuliahViewModel(
    private val repositoryMataKuliah: RepositoryMatakuliah
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> = repositoryMataKuliah.getAllMatakuliah()
        .filterNotNull()
        .map {
            HomeUiState(
                listMataKuliah = it.toList(),
                isLoading = false,
            )
        }
        .onStart {
            emit(HomeUiState(isLoading = true))
            delay(900) // Delay optional, sesuai kebutuhan UI
        }
        .catch { exception ->
            emit(
                HomeUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = exception.message ?: "Terjadi Kesalahan"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(
                isLoading = true,
            )
        )
}

data class HomeUiState(
    val listMataKuliah: List<MataKuliah> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)