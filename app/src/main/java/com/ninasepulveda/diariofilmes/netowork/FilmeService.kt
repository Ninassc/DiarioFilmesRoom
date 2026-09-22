package com.ninasepulveda.diariofilmes.netowork

import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class  FilmeResponse(
    val docs : List<FilmeDoc>
)

data class FilmeDoc(
    val title : String,
    val director : String,
    val description : String
)

interface FilmeService {
    @GET("search.json")
    suspend fun buscarFilmes (
        @Query("q") query: String,
        @Query("limit") limit : Int = 20
    ) : FilmeResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://ghibliapi.dev/films/"

    val api: FilmeService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FilmeService::class.java)
    }
}