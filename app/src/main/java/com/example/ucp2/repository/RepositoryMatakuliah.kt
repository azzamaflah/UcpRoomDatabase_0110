package com.example.ucp2.repository

import com.example.ucp2.data.entity.MataKuliah
import kotlinx.coroutines.flow.Flow

interface RepositoryMatakuliah {
    suspend fun insertMatakuliah(mataKuliah: MataKuliah)
    fun getAllMatakuliah(): Flow<List<MataKuliah>>
    fun getMatakuliah(kode: String): Flow<MataKuliah>
    suspend fun deleteMatakuliah(mataKuliah: MataKuliah)
    suspend fun updateMatakuliah(mataKuliah: MataKuliah)
}