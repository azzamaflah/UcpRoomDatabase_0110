package com.example.ucp2.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ucp2.data.entity.Dosen
import kotlinx.coroutines.flow.Flow

@Dao
interface DosenDao {
    @Query("select * from dosen ORDER BY nama ASC")
    fun getAllDosen() : Flow<List<Dosen>>

    @Insert
    suspend fun inserDosen(dosen: Dosen)

    @Query("SELECT * FROM dosen where nidn = :nidn")
    fun getDosen(nidn: String): Flow<Dosen>

    @Delete
    suspend fun deleteDosen(dosen: Dosen)

    @Update
    suspend fun updateDosen(dosen: Dosen)
}