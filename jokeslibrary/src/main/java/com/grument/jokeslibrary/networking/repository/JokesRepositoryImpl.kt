package com.grument.jokeslibrary.networking.repository

import com.grument.jokeslibrary.data.Joke
import com.grument.jokeslibrary.networking.response.JokeResponse
import com.grument.jokeslibrary.data.Result
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class JokesRepositoryImpl : JokesRepository {

    private val httpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    override suspend fun getAnyJoke(): Result<Joke> {

        return withContext(Dispatchers.IO) {

            val url = "http://api.icndb.com/jokes/random"

            kotlin.runCatching {
                httpClient.get<JokeResponse>(url)
            }.fold(
                onSuccess = { response -> Result.Success(Joke(response.jokeInfo.joke)) },
                onFailure = { Result.Error(it) }
            )
        }
    }
}