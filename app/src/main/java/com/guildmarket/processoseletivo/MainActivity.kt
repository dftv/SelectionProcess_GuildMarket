package com.guildmarket.processoseletivo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.guildmarket.processoseletivo.activities.products.AddProductActivity
import com.guildmarket.processoseletivo.adapters.ProductsAdapter
import com.guildmarket.processoseletivo.api.controllers.ProductsController
import com.guildmarket.processoseletivo.databinding.ActivityMainBinding
import com.guildmarket.processoseletivo.utils.products.ProductsWidgetUtil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var productsAdapter: ProductsAdapter

    private lateinit var productsController: ProductsController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Init View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Init Products Widget
        initProductsWidget()

        // Set click listeners
        binding.SyncFAB.setOnClickListener { it ->
            it.isEnabled = false
            ProductsWidgetUtil().showLoading(binding)

            // Get Products
            productsController.getAll(true, callback = {
                productsAdapter.setProducts(it)

                if (it.isNotEmpty()) ProductsWidgetUtil().showProducts(binding)
                else ProductsWidgetUtil().showEmptyProducts(binding)
            })

            it.isEnabled = true
        }

        binding.AddFAB.setOnClickListener {it ->
            it.isEnabled = false

            val intent = Intent(this, AddProductActivity::class.java)
            registerProductActivity.launch(intent)

            it.isEnabled = true
        }
    }

    private fun initProductsWidget(){
        // Show Loading
        ProductsWidgetUtil().showLoading(binding)

        // Instantiate Products Adapter
        productsAdapter = ProductsAdapter(registerProductActivity)
        binding.ProductsWidget.layoutManager = LinearLayoutManager(this)
        binding.ProductsWidget.adapter = productsAdapter

        // Instantiate Products Controller
        productsController = ProductsController(this)

        // Get Products
        productsController.getAll(false, callback = {
            productsAdapter.setProducts(it)

            if (it.isNotEmpty()) ProductsWidgetUtil().showProducts(binding)
            else ProductsWidgetUtil().showEmptyProducts(binding)
        })
    }

    private val registerProductActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {x ->
        if (x.resultCode == Activity.RESULT_OK) {
            productsController.getAll(false, callback = { y ->
                productsAdapter.setProducts(y)

                if (y.isNotEmpty()) ProductsWidgetUtil().showProducts(binding)
                else ProductsWidgetUtil().showEmptyProducts(binding)
            })
        }
    }
}