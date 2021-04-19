package com.grument.jokeslibrary.networking.response

import com.google.gson.annotations.SerializedName



data class JokeResponse(@SerializedName("type") val type: String,
                        @SerializedName("value") val jokeInfo: JokeInfo)

class JokeInfo(@SerializedName("id") val id: String,
               @SerializedName("joke") val joke: String)