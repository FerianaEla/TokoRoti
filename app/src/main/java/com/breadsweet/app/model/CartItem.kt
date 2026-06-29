package com.breadsweet.app.model

data class CartItem(
    val cartId: String,
    val product: Product,
    val quantity: Int,
    val selectedVariant: String
)
