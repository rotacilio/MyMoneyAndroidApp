package br.com.rotacilio.mymoney.requests.services

import br.com.rotacilio.mymoney.domain.Brand
import retrofit2.Call
import retrofit2.http.GET

interface BrandsServices {

    @GET("brands")
    fun findAll(): Call<List<Brand>>
}