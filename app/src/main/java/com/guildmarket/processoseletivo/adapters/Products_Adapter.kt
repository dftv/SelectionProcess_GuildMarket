package com.guildmarket.processoseletivo.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.guildmarket.processoseletivo.activities.products.AddProductActivity
import com.guildmarket.processoseletivo.activities.products.EditProductActivity
import com.guildmarket.processoseletivo.data.repositories.ProductsRepository
import com.guildmarket.processoseletivo.databinding.ProductWidgetBinding
import com.guildmarket.processoseletivo.models.ProductModel
import java.io.Serializable

class ProductsAdapter(val registerProductActivity: ActivityResultLauncher<Intent>) : RecyclerView.Adapter<ProductsAdapter.ProductHolder>() {
    private var products: List<ProductModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val itemBinding = ProductWidgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductHolder(itemBinding)
    }

    override fun onBindViewHolder(productHolder: ProductHolder, position: Int) {
        productHolder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    inner class ProductHolder(val itemBinding: ProductWidgetBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(product: ProductModel) {
            with(product) {
                // Set the product data to the itemBinding
                itemBinding.product = product

                // Set click listeners
                itemBinding.EditButton.setOnClickListener {it ->
                    it.isEnabled = false

                    val context = itemBinding.root.context
                    val intent = Intent(context, EditProductActivity::class.java).apply {
                        putExtra("product", product)
                    }
                    registerProductActivity.launch(intent)
                }

                itemBinding.EraseButton.setOnClickListener {it ->
                    it.isEnabled = false

                    AlertDialog.Builder(itemBinding.root.context)
                        .setTitle("Deletar produto - ID: $id")
                        .setMessage("Você tem certeza que deseja deletar este produto?")
                        .setPositiveButton("Sim") { dialog, _ ->
                            // Delete the product
                            val productsRepository = ProductsRepository(itemBinding.root.context)
                            productsRepository.easeById(id)

                            // Update the products list
                            notifyItemRemoved(bindingAdapterPosition)

                            dialog.dismiss()

                            it.isEnabled = true
                        }
                        .setNegativeButton("Não") { dialog, _ ->
                            dialog.dismiss()

                            it.isEnabled = true
                        }
                        .show()
                }
            }
        }
    }

    fun setProducts(products: List<ProductModel>) {
        val oldSize = this.products.size

        this.products = products
        notifyItemRangeChanged(0, oldSize)
    }
}