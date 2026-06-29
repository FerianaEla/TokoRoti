package com.breadsweet.app.model

data class Order(
    val id: String,
    val customer: String,
    val items: String,
    val total: Double,
    var status: String, // "pending", "diproses", "selesai"
    val time: String,
    val method: String // "Home Delivery", "Pick-up"
)
