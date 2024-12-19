package com.example.ucp2.repository

import com.example.ucp2.data.dao.MataKuliahDao
import com.example.ucp2.data.entity.MataKuliah
import kotlinx.coroutines.flow.Flow

class LocalRepositoryMatakuliah (
    private val mataKuliahDao : MataKuliah
) : RepositoryMatakuliah {

    override suspend fun inserMatakuliah(mataKuliah: MataKuliah) {
        mataKuliahDao.insertMatakuliah(mataKuliah)
    }

    override fun getAllMatakuliah(): Flow<List<MataKuliah>> {
        return mataKuliahDao.getAllMatakuliah()
    }
    override fun getMatakuliah(kode: String): Flow<MataKuliah>{
        return mataKuliahDao.getMatakuliah(kode)
    }
    override suspend fun deleteMatakuliah(matakuliah: MataKuliah){
        mataKuliahDao.deleteMatakuliah(matakuliah)
    }
    override suspend fun updateMatakuliah(matakuliah: MataKuliah){
        mataKuliahDao.updateMatakuliah(matakuliah)
    }
}
