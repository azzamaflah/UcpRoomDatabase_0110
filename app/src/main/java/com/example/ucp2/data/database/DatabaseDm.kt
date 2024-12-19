package com.example.ucp2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ucp2.data.dao.DosenDao
import com.example.ucp2.data.dao.MataKuliahDao
import com.example.ucp2.data.entity.Dosen

@Database(entities = [Dosen::class], version = 1, exportSchema = false)
abstract class DatabaseDm : RoomDatabase() {

    abstract fun dosenDao(): DosenDao
    abstract fun matakuliahDao(): MataKuliahDao

    companion object {
        @Volatile
        private var Instance: DatabaseDm? = null

        fun getDatabase(context: Context): DatabaseDm {
            return (Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    DatabaseDm::class.java,
                    "DosenDatabase"

                )
                    .build().also { Instance = it }
            })
        }
    }

}