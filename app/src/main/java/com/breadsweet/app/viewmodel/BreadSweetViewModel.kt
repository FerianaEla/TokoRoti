package com.breadsweet.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breadsweet.app.data.BreadSweetData
import com.breadsweet.app.data.OrderRepository
import com.breadsweet.app.data.ProductRepository
import com.breadsweet.app.model.*
import kotlinx.coroutines.launch

class BreadSweetViewModel : ViewModel() {

    // --- Authentication State ---
    var currentUser by mutableStateOf<User?>(null)
        private set

    val isAuthenticated: Boolean
        get() = currentUser != null

    private val mockAdmin = User(
        id = "admin-001",
        email = "admin@breadsweet.com",
        name = "Admin BreadSweet",
        role = UserRole.ADMIN
    )

    fun login(email: String, password: String): Pair<Boolean, UserRole?> {
        if (email == "admin@breadsweet.com" && password == "admin123") {
            currentUser = mockAdmin
            return Pair(true, UserRole.ADMIN)
        }
        if (email.isNotBlank() && password.length >= 6) {
            val user = User(
                id = "cust-${System.currentTimeMillis()}",
                email = email,
                name = email.substringBefore("@"),
                role = UserRole.CUSTOMER
            )
            currentUser = user
            return Pair(true, UserRole.CUSTOMER)
        }
        return Pair(false, null)
    }

    fun register(email: String, password: String, name: String): Boolean {
        if (email.isNotBlank() && password.length >= 6 && name.isNotBlank()) {
            val user = User(
                id = "cust-${System.currentTimeMillis()}",
                email = email,
                name = name,
                role = UserRole.CUSTOMER
            )
            currentUser = user
            return true
        }
        return false
    }

    fun logout() {
        currentUser = null
        clearCart()
    }

    fun updateProfile(name: String, phone: String, address: String) {
        currentUser?.let { user ->
            currentUser = user.copy(name = name, phone = phone, address = address)
        }
    }

    // --- Products Catalog State ---
    val products = mutableStateListOf<Product>().apply {
        addAll(BreadSweetData.INITIAL_PRODUCTS)
    }

    var isFetchingProducts by mutableStateOf(false)
        private set

    fun fetchProductsFromCloud() {
        if (isFetchingProducts) return
        isFetchingProducts = true
        viewModelScope.launch {
            try {
                val cloudProducts = ProductRepository.getProducts()
                if (cloudProducts.isNotEmpty()) {
                    val cloudIds = cloudProducts.map { it.id }.toSet()
                    val missingLocal = BreadSweetData.INITIAL_PRODUCTS.filter { it.id !in cloudIds }
                    
                    products.clear()
                    products.addAll(cloudProducts)
                    
                    if (missingLocal.isNotEmpty()) {
                        products.addAll(missingLocal)
                        ProductRepository.saveProducts(products.toList())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isFetchingProducts = false
            }
        }
    }

    fun addProduct(
        name: String,
        category: String,
        variants: List<String>,
        price: Double,
        image: String,
        description: String,
        stock: Int
    ) {
        val newProduct = Product(
            id = System.currentTimeMillis(),
            name = name,
            category = category,
            variants = if (variants.isEmpty()) listOf("Original") else variants,
            price = price,
            image = if (image.isBlank()) "android.resource://com.breadsweet.app/drawable/roti_sobek_cokelat_keju" else image,
            description = if (description.isBlank()) "$name dari BreadSweet" else description,
            rating = 5.0,
            sold = 0,
            stock = stock,
            isNew = true
        )
        products.add(0, newProduct)

        // Sync to cloud
        viewModelScope.launch {
            ProductRepository.saveProducts(products.toList())
        }
    }

    fun editProduct(
        id: Long,
        name: String,
        category: String,
        variants: List<String>,
        price: Double,
        image: String,
        description: String,
        stock: Int
    ) {
        val index = products.indexOfFirst { it.id == id }
        if (index != -1) {
            val old = products[index]
            products[index] = old.copy(
                name = name,
                category = category,
                variants = if (variants.isEmpty()) listOf("Original") else variants,
                price = price,
                image = if (image.isBlank()) old.image else image,
                description = description,
                stock = stock
            )

            // Sync to cloud
            viewModelScope.launch {
                ProductRepository.saveProducts(products.toList())
            }
        }
    }

    fun deleteProduct(id: Long) {
        products.removeAll { it.id == id }

        // Sync to cloud
        viewModelScope.launch {
            ProductRepository.saveProducts(products.toList())
        }
    }

    // --- Cart State ---
    val cartItems = mutableStateListOf<CartItem>()

    val totalCartItems: Int
        get() = cartItems.sumOf { it.quantity }

    val totalCartPrice: Double
        get() = cartItems.sumOf { it.product.price * it.quantity }

    fun addToCart(product: Product, variant: String? = null) {
        val selectedVariant = variant ?: product.variants.firstOrNull() ?: "Original"
        val cartId = "${product.id}-$selectedVariant"

        val index = cartItems.indexOfFirst { it.cartId == cartId }
        if (index != -1) {
            val item = cartItems[index]
            cartItems[index] = item.copy(quantity = item.quantity + 1)
        } else {
            cartItems.add(CartItem(cartId, product, 1, selectedVariant))
        }
    }

    fun removeFromCart(cartId: String) {
        cartItems.removeAll { it.cartId == cartId }
    }

    fun updateQuantity(cartId: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(cartId)
            return
        }
        val index = cartItems.indexOfFirst { it.cartId == cartId }
        if (index != -1) {
            cartItems[index] = cartItems[index].copy(quantity = quantity)
        }
    }

    fun clearCart() {
        cartItems.clear()
    }

    // --- Orders State ---
    val orders = mutableStateListOf<Order>().apply {
        addAll(
            listOf(
                Order("BS20260608-001", "Rina Maulida", "Croissant Butter Original ×2, Pain au Chocolat ×1", 68000.0, "pending", "08:12", "Home Delivery"),
                Order("BS20260608-002", "Budi Santoso", "Roti Sobek Cokelat Keju ×1, Roti Bun Kopi ×2", 42000.0, "diproses", "08:45", "Pick-up"),
                Order("BS20260608-003", "Dewi Putri", "Cinnamon Roll Kayu Manis ×3, Roti Donat ×2", 61000.0, "diproses", "09:03", "Home Delivery"),
                Order("BS20260608-004", "Arif Rahman", "Pizza Mini Sosis Keju ×3, Roti Sosis ×2", 68000.0, "selesai", "07:30", "Pick-up")
            )
        )
    }

    var isFetchingOrders by mutableStateOf(false)
        private set

    fun fetchOrdersFromCloud() {
        if (isFetchingOrders) return
        isFetchingOrders = true
        viewModelScope.launch {
            try {
                val cloudOrders = OrderRepository.getOrders()
                if (cloudOrders.isNotEmpty()) {
                    orders.clear()
                    orders.addAll(cloudOrders)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isFetchingOrders = false
            }
        }
    }

    init {
        fetchOrdersFromCloud()
        fetchProductsFromCloud()
    }

    fun addOrder(customerName: String, itemsSummary: String, totalAmount: Double, deliveryMethod: String) {
        val calendar = java.util.Calendar.getInstance()
        val hour = String.format("%02d", calendar.get(java.util.Calendar.HOUR_OF_DAY))
        val minute = String.format("%02d", calendar.get(java.util.Calendar.MINUTE))
        val dateString = String.format("%04d%02d%02d", 
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH) + 1,
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        )
        val id = "BS$dateString-${System.currentTimeMillis() % 1000}"
        val order = Order(
            id = id,
            customer = customerName,
            items = itemsSummary,
            total = totalAmount,
            status = "pending",
            time = "$hour:$minute",
            method = deliveryMethod
        )
        orders.add(0, order)

        // Sync to cloud
        viewModelScope.launch {
            OrderRepository.saveOrders(orders.toList())
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        val index = orders.indexOfFirst { it.id == orderId }
        if (index != -1) {
            orders[index] = orders[index].copy(status = newStatus)

            // Sync to cloud
            viewModelScope.launch {
                OrderRepository.saveOrders(orders.toList())
            }
        }
    }

    // --- Search and Category UI State ---
    var searchQuery by mutableStateOf("")
    var activeCategory by mutableStateOf("Semua")
}
