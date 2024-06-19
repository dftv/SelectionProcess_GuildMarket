package com.guildmarket.processoseletivo.data.repositories

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.guildmarket.processoseletivo.data.SQLiteHelper
import com.guildmarket.processoseletivo.models.ProductModel

class ProductsRepository(context: Context) {
    private val sqLiteHelper = SQLiteHelper(context)

    // Add functions
    fun addAll(products: List<ProductModel>) {
        val db = sqLiteHelper.writableDatabase
        db.beginTransaction()

        try {
            products.forEach {
                    product ->
                val values = ContentValues().apply {
                    put(SQLiteHelper.COLUMN_TITLE, product.title)
                    put(SQLiteHelper.COLUMN_PRICE, product.price)
                    put(SQLiteHelper.COLUMN_DESCRIPTION, product.description)
                }
                db.insert(SQLiteHelper.TABLE_NAME, null, values)
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun add(product: ProductModel) {
        val db = sqLiteHelper.writableDatabase
        db.beginTransaction()

        try {
            val values = ContentValues().apply {
                put(SQLiteHelper.COLUMN_TITLE, product.title)
                put(SQLiteHelper.COLUMN_PRICE, product.price)
                put(SQLiteHelper.COLUMN_DESCRIPTION, product.description)
            }
            db.insert(SQLiteHelper.TABLE_NAME, null, values)

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    // Get functions
    fun getAll(): List<ProductModel> {
        val db = sqLiteHelper.readableDatabase
        db.beginTransaction()
        val products = mutableListOf<ProductModel>()

        try {
            val cursor = db.query(SQLiteHelper.TABLE_NAME, null, null, null, null, null, null)

            with(cursor) {
                while (moveToNext()) {
                    val product = ProductModel(
                        id = getInt(getColumnIndexOrThrow(SQLiteHelper.COLUMN_ID)),
                        title = getString(getColumnIndexOrThrow(SQLiteHelper.COLUMN_TITLE)),
                        price = getDouble(getColumnIndexOrThrow(SQLiteHelper.COLUMN_PRICE)),
                        description = getString(getColumnIndexOrThrow(SQLiteHelper.COLUMN_DESCRIPTION))
                    )
                    products.add(product)
                }
            }

            cursor.close()
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }

        return products
    }

    fun getById(id: Int): ProductModel {
        val db = sqLiteHelper.readableDatabase
        db.beginTransaction()

        var product : ProductModel

        try {
            val cursor = db.query(SQLiteHelper.TABLE_NAME, null, "${SQLiteHelper.COLUMN_ID}=?", arrayOf(id.toString()), null, null, null)
            cursor.moveToFirst()

            product = ProductModel(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_ID)),
                title = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_TITLE)),
                price = cursor.getDouble(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_PRICE)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_DESCRIPTION))
            )

            cursor.close()
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }

        return product
    }

    // Edit function
    fun edit(product: ProductModel) {
        val db = sqLiteHelper.writableDatabase
        db.beginTransaction()

        try {
            val values = ContentValues().apply {
                put(SQLiteHelper.COLUMN_TITLE, product.title)
                put(SQLiteHelper.COLUMN_PRICE, product.price)
                put(SQLiteHelper.COLUMN_DESCRIPTION, product.description)
            }
            db.update(SQLiteHelper.TABLE_NAME, values, "${SQLiteHelper.COLUMN_ID}=?", arrayOf(product.id.toString()))

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    // Delete functions
    fun easeAll() {
        val db = sqLiteHelper.writableDatabase
        db.beginTransaction()

        try {
            db.delete(SQLiteHelper.TABLE_NAME, null, null).toLong()
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='${SQLiteHelper.TABLE_NAME}'")

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }

    }

    fun easeById(id: Int?) {
        if (id == null) return

        val db = sqLiteHelper.writableDatabase
        db.beginTransaction()

        try {
            db.delete(SQLiteHelper.TABLE_NAME, "${SQLiteHelper.COLUMN_ID}=?", arrayOf(id.toString())).toLong()

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    // Check if table is empty
    fun isEmpty(): Boolean {
        val db = sqLiteHelper.readableDatabase
        db.beginTransaction()

        var isEmpty : Boolean

        try {
            val cursor = db.query(SQLiteHelper.TABLE_NAME, null, null, null, null, null, null)
            isEmpty = cursor.count == 0

            cursor.close()
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }

        return isEmpty
    }
}