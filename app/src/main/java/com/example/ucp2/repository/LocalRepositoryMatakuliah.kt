package com.example.ucp2.repository

import com.example.ucp2.data.dao.MataKuliahDao
import com.example.ucp2.data.entity.MataKuliah
import kotlinx.coroutines.flow.Flow

class LocalRepositoryMatakuliah (
    private val mataKuliahDao : MataKuliahDao
) : RepositoryMatakuliah {

    override suspend fun insertMatakuliah(mataKuliah: MataKuliah) {
        mataKuliahDao.insertMataKuliah(mataKuliah)
    }

    override fun getAllMatakuliah(): Flow<List<MataKuliah>> {
        return mataKuliahDao.getAllMataKuliah()
    }
    override fun getMatakuliah(kode: String): Flow<MataKuliah>{
        return mataKuliahDao.getMataKuliah(kode)
    }
    override suspend fun deleteMatakuliah(matakuliah: MataKuliah){
        mataKuliahDao.deleteMatakuliah(matakuliah)
    }
    override suspend fun updateMatakuliah(matakuliah: MataKuliah){
        mataKuliahDao.updateMatakuliah(matakuliah)
    }
}
