package com.breadsweet.app.model

enum class UserRole {
    CUSTOMER, ADMIN
}

data class User(
    val id: String,
    val email: String,
    val name: String,
    val phone: String? = null,
    val address: String? = null,
    val role: UserRole = UserRole.CUSTOMER
)
