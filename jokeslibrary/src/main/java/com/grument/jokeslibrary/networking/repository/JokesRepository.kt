package com.grument.jokeslibrary.networking.repository

import com.grument.jokeslibrary.data.Joke
import com.grument.jokeslibrary.data.Result

interface JokesRepository {

    suspend fun getAnyJoke(): Result<Joke>
}