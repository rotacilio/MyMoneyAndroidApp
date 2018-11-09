package br.com.rotacilio.mymoney.requests.services

import br.com.rotacilio.mymoney.domain.Card
import okhttp3.CacheControl
import retrofit2.Call
import retrofit2.http.*

interface CardsServices {

    @GET("cards")
    fun findAll(@Header("Cache-Control") cacheControl: String?): Call<List<Card>>

    @DELETE("cards/{cardId}")
    fun deleteCardById(@Path("cardId") cardId: String): Call<Boolean>

    @POST("cards")
    fun createNewCard(@Body card: Card): Call<Card>
}