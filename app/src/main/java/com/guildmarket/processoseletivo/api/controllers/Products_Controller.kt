package com.guildmarket.processoseletivo.api.controllers

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.guildmarket.processoseletivo.api.RetrofitAPI
import com.guildmarket.processoseletivo.api.interfaces.ProductsInterface
import com.guildmarket.processoseletivo.data.repositories.ProductsRepository
import com.guildmarket.processoseletivo.models.ProductModel
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class ProductsController(val context: Context, ) {
    private val productsInterface: ProductsInterface = RetrofitAPI.instance.create(ProductsInterface::class.java)

    fun getAll(forceSync: Boolean, callback: (List<ProductModel>) -> Unit) {
        var products: List<ProductModel> = emptyList()

        val call = productsInterface.getProducts()

        call.enqueue(object : Callback<List<ProductModel>> {
            override fun onResponse(call: Call<List<ProductModel>>, response: Response<List<ProductModel>>) {
                // Get products from API
                val apiProducts = response.body()?.toList() ?: emptyList()

                // Save and get products in SQLite
                val productsRepository = ProductsRepository(context)

                if (forceSync) {
                    productsRepository.easeAll()
                }

                if (productsRepository.isEmpty()) {
                    productsRepository.addAll(apiProducts)
                }

                products = productsRepository.getAll()

                callback(products)
            }

            override fun onFailure(call: Call<List<ProductModel>>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                callback(products)
            }
        })
    }

    fun add(product: ProductModel) : Boolean {
        val productsRepository = ProductsRepository(context)

        productsRepository.add(product)

        return true
    }

    fun edit(product: ProductModel) : Boolean {
        val productsRepository = ProductsRepository(context)

        productsRepository.edit(product)

        return true
    }
}