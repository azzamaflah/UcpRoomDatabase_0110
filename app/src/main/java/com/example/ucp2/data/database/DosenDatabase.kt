package com.example.ucp2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ucp2.data.dao.DosenDao
import com.example.ucp2.data.entity.Dosen

@Database(entities = [Dosen::class], version = 1, exportSchema = false)
abstract class DosenDatabase : RoomDatabase(){

    abstract fun dosenDao(): DosenDao

    companion object{
        @Volatile
        private var Instance: DosenDatabase? = null

        fun getDatabase(context: Context): DosenDatabase {
            return (Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    DosenDatabase::class.java,
                    "DosenDatabase"

                )
                    .build().also { Instance = it }
            })
        }
    }

}