package com.guildmarket.processoseletivo.utils.products

import android.view.View
import com.guildmarket.processoseletivo.databinding.ActivityMainBinding

class ProductsWidgetUtil {
    fun showProducts(binding: ActivityMainBinding){
        binding.ProductsWidget.visibility = View.VISIBLE
        binding.EmptyProducts.visibility =  View.GONE
        binding.ProgressBarProducts.visibility = View.GONE
    }

    fun showEmptyProducts(binding: ActivityMainBinding){
        binding.ProductsWidget.visibility = View.GONE
        binding.EmptyProducts.visibility =  View.VISIBLE
        binding.ProgressBarProducts.visibility = View.GONE
    }

    fun showLoading(binding: ActivityMainBinding){
        binding.ProductsWidget.visibility = View.GONE
        binding.EmptyProducts.visibility =  View.GONE
        binding.ProgressBarProducts.visibility = View.VISIBLE
    }
}