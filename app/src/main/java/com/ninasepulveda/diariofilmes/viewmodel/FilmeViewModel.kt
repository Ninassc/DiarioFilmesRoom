package com.ninasepulveda.diariofilmes.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ninasepulveda.diariofilmes.database.FilmeDatabase
import com.ninasepulveda.diariofilmes.model.Filme
import com.ninasepulveda.diariofilmes.repository.FilmeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface BuscaState {
    object Idle : BuscaState
    object Loading : BuscaState
    data class Error(val message: String) : BuscaState
}

class FilmeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FilmeRepository(
        dao = FilmeDatabase.getInstance(application).filmeDao()
    )

    val filmes : StateFlow<List<Filme>> = repository.observeFilmes().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()

    )

    var buscaState: BuscaState by mutableStateOf(BuscaState.Idle)
        private set

    fun buscar(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            buscaState = BuscaState.Loading
            buscaState = try {
                repository.buscarEArmazenar(query)
                BuscaState.Idle
            } catch (e: Exception) {
                BuscaState.Error("Não foi possível buscar agora. Mostrando livros salvos.")
            }
        }
    }
}