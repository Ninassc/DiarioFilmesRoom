package com.ninasepulveda.diariofilmes.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filmes")
data class FilmeEntity(
    @PrimaryKey(autoGenerate = true) val id : Long = 0,
    val titulo : String,
    val diretor : String,
    val descricao : String
)