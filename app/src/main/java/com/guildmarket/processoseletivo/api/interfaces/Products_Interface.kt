package com.guildmarket.processoseletivo.api.interfaces

import com.guildmarket.processoseletivo.models.ProductModel
import retrofit2.Call
import retrofit2.http.GET

interface ProductsInterface {
    @GET("products")
    fun getProducts(): Call<List<ProductModel>>
}