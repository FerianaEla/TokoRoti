package com.breadsweet.app.model

data class Product(
    val id: Long,
    val name: String,
    val category: String,
    val variants: List<String>,
    val price: Double,
    val originalPrice: Double? = null,
    val image: String,
    val description: String,
    val ingredients: List<String> = emptyList(),
    val rating: Double,
    val sold: Int,
    val stock: Int,
    val isNew: Boolean = false,
    val isPromo: Boolean = false
)
