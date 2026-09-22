package com.ninasepulveda.diariofilmes.repository

import com.ninasepulveda.diariofilmes.database.FilmeDao
import com.ninasepulveda.diariofilmes.database.FilmeEntity
import com.ninasepulveda.diariofilmes.model.Filme
import com.ninasepulveda.diariofilmes.netowork.FilmeDoc
import com.ninasepulveda.diariofilmes.netowork.FilmeService
import com.ninasepulveda.diariofilmes.netowork.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.http.Query

class FilmeRepository(
    private val dao: FilmeDao,
    private val api: FilmeService = RetrofitClient.api
) {
    fun observeFilmes(): Flow<List<Filme>> =
        dao.observeAll().map { entidades ->
            entidades.map { it.toDomain() }
        }

    suspend fun buscarEArmazenar(query: String) {
        val resposta = api.buscarFilmes(query)
        dao.insertAll(resposta.docs.map { it.toEntity() })
    }
}

private fun FilmeEntity.toDomain() = Filme(id = id, titulo = titulo, diretor = diretor, descricao = descricao )

private fun FilmeDoc.toEntity() = FilmeEntity(
    titulo = title,
    diretor = director,
    descricao = description
)