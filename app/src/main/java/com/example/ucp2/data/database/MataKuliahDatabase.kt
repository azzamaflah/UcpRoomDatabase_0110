package com.example.ucp2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ucp2.data.dao.MataKuliahDao
import com.example.ucp2.data.entity.MataKuliah

@Database(entities = [MataKuliah::class], version = 1, exportSchema = false)
abstract class MataKuliahDatabase : RoomDatabase(){

    abstract fun matakuliahDao(): MataKuliahDao

    companion object{
        @Volatile
        private var Instance: MataKuliahDatabase? = null

        fun getDatabase(context: Context): MataKuliahDatabase {
            return (Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    MataKuliahDatabase::class.java,
                    "MataKuliahDatabase"

                )
                    .build().also { Instance = it }
            })
        }
    }

}