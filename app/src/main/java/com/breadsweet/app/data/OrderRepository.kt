package com.breadsweet.app.data

import com.breadsweet.app.model.Order
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object OrderRepository {
    private const val BLOB_URL = "https://jsonblob.com/api/jsonBlob/019f11e5-e10d-7b30-9657-d7cad7545a61"

    suspend fun getOrders(): List<Order> = withContext(Dispatchers.IO) {
        val ordersList = mutableListOf<Order>()
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
                    ordersList.add(
                        Order(
                            id = obj.getString("id"),
                            customer = obj.getString("customer"),
                            items = obj.getString("items"),
                            total = obj.getDouble("total"),
                            status = obj.getString("status"),
                            time = obj.getString("time"),
                            method = obj.getString("method")
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
        }
        ordersList
    }

    suspend fun saveOrders(orders: List<Order>): Boolean = withContext(Dispatchers.IO) {
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
            for (order in orders) {
                val obj = JSONObject()
                obj.put("id", order.id)
                obj.put("customer", order.customer)
                obj.put("items", order.items)
                obj.put("total", order.total)
                obj.put("status", order.status)
                obj.put("time", order.time)
                obj.put("method", order.method)
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
