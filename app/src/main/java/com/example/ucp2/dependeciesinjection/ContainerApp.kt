package com.example.ucp2.dependeciesinjection

import android.content.Context
import com.example.ucp2.data.database.DatabaseDm
import com.example.ucp2.repository.LocalRepositoryDosen
import com.example.ucp2.repository.LocalRepositoryMatakuliah
import com.example.ucp2.repository.RepositoryDosen
import com.example.ucp2.repository.RepositoryMatakuliah

interface InterfaceContainerApp{
    val repositoryDosen: RepositoryDosen
    val repositoryMatakuliah: RepositoryMatakuliah

}

class ContainerApp(private val context: Context) : InterfaceContainerApp {
    override val repositoryDosen: RepositoryDosen by lazy {
        LocalRepositoryDosen(DatabaseDm.getDatabase(context).dosenDao())
    }

    override val repositoryMatakuliah: RepositoryMatakuliah by lazy {
        LocalRepositoryMatakuliah(DatabaseDm.getDatabase(context).matakuliahDao())
    }

}