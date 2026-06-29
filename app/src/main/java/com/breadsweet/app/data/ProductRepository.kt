package com.breadsweet.app.data

import com.breadsweet.app.model.Product
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ProductRepository {
    private const val BLOB_URL = "https://jsonblob.com/api/jsonBlob/019f11e6-7db8-7cb6-8d3d-9c683d0c1d45"

    suspend fun getProducts(): List<Product> = withContext(Dispatchers.IO) {
        val productsList = mutableListOf<Product>()
        var connection: HttpURLConnection? = null
        try {
            val url = URL(BLOB_URL)
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 8000
            connection.readTimeout = 8000
            connection.setRequestProperty("Accept", "application/json")

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val jsonString = reader.use { it.readText() }
                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    
                    val variantsArray = obj.optJSONArray("variants")
                    val variantsList = mutableListOf<String>()
                    if (variantsArray != null) {
                        for (j in 0 until variantsArray.length()) {
                            variantsList.add(variantsArray.getString(j))
                        }
                    }

                    val ingredientsArray = obj.optJSONArray("ingredients")
                    val ingredientsList = mutableListOf<String>()
                    if (ingredientsArray != null) {
                        for (j in 0 until ingredientsArray.length()) {
                            ingredientsList.add(ingredientsArray.getString(j))
                        }
                    }

                    val originalPrice = if (obj.isNull("originalPrice")) null else obj.optDouble("originalPrice")

                    productsList.add(
                        Product(
                            id = obj.getLong("id"),
                            name = obj.getString("name"),
                            category = obj.getString("category"),
                            variants = variantsList,
                            price = obj.getDouble("price"),
                            originalPrice = originalPrice,
                            image = obj.getString("image"),
                            description = obj.getString("description"),
                            ingredients = ingredientsList,
                            rating = obj.optDouble("rating", 5.0),
                            sold = obj.optInt("sold", 0),
                            stock = obj.getInt("stock"),
                            isNew = obj.optBoolean("isNew", false),
                            isPromo = obj.optBoolean("isPromo", false)
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
        }
        productsList
    }

    suspend fun saveProducts(products: List<Product>): Boolean = withContext(Dispatchers.IO) {
        var connection: HttpURLConnection? = null
        try {
            val url = URL(BLOB_URL)
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "PUT"
            connection.connectTimeout = 8000
            connection.readTimeout = 8000
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")

            val jsonArray = JSONArray()
            for (product in products) {
                val obj = JSONObject()
                obj.put("id", product.id)
                obj.put("name", product.name)
                obj.put("category", product.category)

                val variantsArray = JSONArray()
                product.variants.forEach { variantsArray.put(it) }
                obj.put("variants", variantsArray)

                obj.put("price", product.price)
                if (product.originalPrice != null) {
                    obj.put("originalPrice", product.originalPrice)
                } else {
                    obj.put("originalPrice", JSONObject.NULL)
                }
                obj.put("image", product.image)
                obj.put("description", product.description)

                val ingredientsArray = JSONArray()
                product.ingredients.forEach { ingredientsArray.put(it) }
                obj.put("ingredients", ingredientsArray)

                obj.put("rating", product.rating)
                obj.put("sold", product.sold)
                obj.put("stock", product.stock)
                obj.put("isNew", product.isNew)
                obj.put("isPromo", product.isPromo)
                
                jsonArray.put(obj)
            }

            val writer = OutputStreamWriter(connection.outputStream)
            writer.use {
                it.write(jsonArray.toString())
                it.flush()
            }

            val responseCode = connection.responseCode
            responseCode == HttpURLConnection.HTTP_OK
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            connection?.disconnect()
        }
    }
}
