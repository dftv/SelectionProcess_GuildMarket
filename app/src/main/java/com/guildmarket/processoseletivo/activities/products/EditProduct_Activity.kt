package com.guildmarket.processoseletivo.activities.products

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.guildmarket.processoseletivo.R
import com.guildmarket.processoseletivo.api.controllers.ProductsController
import com.guildmarket.processoseletivo.databinding.EditproductActivityBinding
import com.guildmarket.processoseletivo.models.ProductModel
import com.guildmarket.processoseletivo.utils.DecimalDigitsInputFilterUtil

class EditProductActivity : AppCompatActivity() {
    private lateinit var binding: EditproductActivityBinding

    private lateinit var productsController: ProductsController

    private lateinit var product: ProductModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Init View Binding
        binding = EditproductActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Init toolbar
        initToolbar()

        // Init Products Controller
        productsController = ProductsController(this)

        // Set price input filter
        binding.PriceEditText.filters = arrayOf(DecimalDigitsInputFilterUtil(2))

        // Get product data
        product = intent.getParcelableExtra("product")!!

        // Set product data to the view
        binding.ProductoId.text = "${getString(R.string.product_id)} ${product.id}"
        binding.TitleEditText.setText(product.title)
        binding.PriceEditText.setText(String.format(getString(R.string.product_price_sample), product.price))
        binding.DescriptionEditText.setText(product.description)

        // Add button click listener
        binding.EditButton.setOnClickListener {it ->
            it.isEnabled = false

            // Get product data
            val title: String = binding.TitleEditText.text.toString()
            val price: Double? = binding.PriceEditText.text.toString().toDoubleOrNull()
            val description: String = binding.DescriptionEditText.text.toString()

            // Validate product data
            var success = false

            when {
                title.isBlank() -> {
                    Toast.makeText(this, R.string.error_product_title, Toast.LENGTH_SHORT).show()
                }
                price == null -> {
                    Toast.makeText(this, R.string.error_product_price, Toast.LENGTH_SHORT).show()
                }
                price < 0 -> {
                    Toast.makeText(this, R.string.error_product_price_invalid, Toast.LENGTH_SHORT).show()
                }
                description.isBlank() -> {
                    Toast.makeText(this, R.string.error_product_description, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Save product
                    val product = ProductModel(product.id, title, price, description)

                    success = productsController.edit(product)
                }
            }


            if (success) {
                Toast.makeText(this, R.string.success_product_edit, Toast.LENGTH_SHORT).show()

                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, R.string.error_product_edit, Toast.LENGTH_SHORT).show()

                it.isEnabled = true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val upArrow = ContextCompat.getDrawable(this, R.drawable.arrow_back_24px)
        upArrow?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this, R.color.light), PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(upArrow)
    }
}