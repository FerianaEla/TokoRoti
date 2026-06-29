package com.breadsweet.app.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import androidx.compose.ui.res.painterResource
import com.breadsweet.app.R
import com.breadsweet.app.data.BreadSweetData
import com.breadsweet.app.data.PromoBanner
import com.breadsweet.app.model.*
import com.breadsweet.app.ui.theme.*
import com.breadsweet.app.viewmodel.BreadSweetViewModel
import kotlinx.coroutines.delay

// ==========================================
// 1. LOGIN SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: BreadSweetViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(600)
            val (success, role) = viewModel.login(email, password)
            isLoading = false
            if (success) {
                if (role == UserRole.ADMIN) {
                    onNavigateToAdmin()
                } else {
                    onNavigateToHome()
                }
            } else {
                errorMsg = "Email atau password salah"
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Amber50)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Header Hero
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Amber700, Amber600, Amber500)
                        ),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.White, RoundedCornerShape(24.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo Toko",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "BreadSweet",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Roti hangat untuk hari yang sempurna",
                        color = Amber50.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }

            // Form container
            Card(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .offset(y = (-24).dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Selamat Datang",
                        color = Amber800,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Masuk ke akun Anda",
                        color = MediumGray,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 24.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; errorMsg = "" },
                        label = { Text("Email") },
                        placeholder = { Text("nama@email.com") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = Amber600) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Amber600,
                            unfocusedBorderColor = BorderColor
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMsg = "" },
                        label = { Text("Password") },
                        placeholder = { Text("Min. 6 karakter") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password", tint = Amber600) },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = Amber500
                                )
                            }
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Amber600,
                            unfocusedBorderColor = BorderColor
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )

                    if (errorMsg.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(RedBg, RoundedCornerShape(12.dp))
                                .border(1.dp, RedText.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(text = errorMsg, color = RedText, fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                errorMsg = "Email dan password wajib diisi"
                            } else if (password.length < 6) {
                                errorMsg = "Password minimal 6 karakter"
                            } else {
                                isLoading = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Amber600),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Masuk", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Default.ArrowForward, contentDescription = "Arrow right")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f), color = BorderColor)
                        Text(
                            text = "atau",
                            modifier = Modifier.padding(horizontal = 12.dp),
                            color = MediumGray,
                            fontSize = 12.sp
                        )
                        Divider(modifier = Modifier.weight(1f), color = BorderColor)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {
                            email = "admin@breadsweet.com"
                            password = "admin123"
                            isLoading = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Amber600),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Amber600)
                    ) {
                        Text("🛡️  Login sebagai Admin", fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.Center) {
                        Text("Belum punya akun? ", color = MediumGray, fontSize = 14.sp)
                        Text(
                            text = "Daftar sekarang",
                            color = Amber700,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { onNavigateToRegister() }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Amber100.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "Demo admin: admin@breadsweet.com / admin123",
                            fontSize = 11.sp,
                            color = Amber800,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. REGISTER SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: BreadSweetViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(600)
            val success = viewModel.register(email, password, name)
            isLoading = false
            if (success) {
                onNavigateToHome()
            } else {
                errorMsg = "Pendaftaran gagal, periksa data Anda"
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Amber50)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Amber700, Amber600)
                        ),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(Color.White, RoundedCornerShape(20.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo Toko",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Daftar Akun Baru",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Mulai belanja roti lezat di BreadSweet",
                        color = Amber50.copy(alpha = 0.8f),
                        fontSize = 13.sp
                    )
                }
            }

            // Form container
            Card(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .offset(y = (-20).dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; errorMsg = "" },
                        label = { Text("Nama Lengkap") },
                        placeholder = { Text("cth. Pelanggan") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name", tint = Amber600) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Amber600,
                            unfocusedBorderColor = BorderColor
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; errorMsg = "" },
                        label = { Text("Email") },
                        placeholder = { Text("nama@email.com") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = Amber600) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Amber600,
                            unfocusedBorderColor = BorderColor
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMsg = "" },
                        label = { Text("Password") },
                        placeholder = { Text("Min. 6 karakter") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password", tint = Amber600) },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = Amber500
                                )
                            }
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Amber600,
                            unfocusedBorderColor = BorderColor
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )

                    if (errorMsg.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(RedBg, RoundedCornerShape(12.dp))
                                .border(1.dp, RedText.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(text = errorMsg, color = RedText, fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                                errorMsg = "Semua kolom wajib diisi"
                            } else if (password.length < 6) {
                                errorMsg = "Password minimal 6 karakter"
                            } else {
                                isLoading = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Amber600),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Daftar Sekarang", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.Center) {
                        Text("Sudah punya akun? ", color = MediumGray, fontSize = 14.sp)
                        Text(
                            text = "Masuk di sini",
                            color = Amber700,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { onNavigateToLogin() }
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. MAIN CONTAINER SCREEN (CUSTOMER TABS)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: BreadSweetViewModel,
    onNavigateToProductDetail: (Long) -> Unit,
    onNavigateToCheckout: () -> Unit,
    onNavigateToLogin: () -> Unit,
    initialTab: Int = 0
) {
    var selectedTab by remember { mutableStateOf(initialTab) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Amber700,
                        selectedTextColor = Amber700,
                        unselectedIconColor = MediumGray,
                        unselectedTextColor = MediumGray,
                        indicatorColor = Amber100
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Menu, contentDescription = "Kategori") },
                    label = { Text("Kategori", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Amber700,
                        selectedTextColor = Amber700,
                        unselectedIconColor = MediumGray,
                        unselectedTextColor = MediumGray,
                        indicatorColor = Amber100
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Box {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Keranjang")
                            if (viewModel.totalCartItems > 0) {
                                Badge(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = 8.dp, y = (-8).dp),
                                    containerColor = Color.Red
                                ) {
                                    Text(
                                        text = viewModel.totalCartItems.toString(),
                                        color = Color.White,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    },
                    label = { Text("Keranjang", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Amber700,
                        selectedTextColor = Amber700,
                        unselectedIconColor = MediumGray,
                        unselectedTextColor = MediumGray,
                        indicatorColor = Amber100
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                    label = { Text("Profil", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Amber700,
                        selectedTextColor = Amber700,
                        unselectedIconColor = MediumGray,
                        unselectedTextColor = MediumGray,
                        indicatorColor = Amber100
                    )
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                0 -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToProductDetail = onNavigateToProductDetail,
                    onSwitchToCategoryTab = { selectedTab = 1 }
                )
                1 -> CategoriesScreen(
                    viewModel = viewModel,
                    onNavigateToProductDetail = onNavigateToProductDetail
                )
                2 -> CartScreen(
                    viewModel = viewModel,
                    onNavigateToCheckout = onNavigateToCheckout
                )
                3 -> ProfileScreen(
                    viewModel = viewModel,
                    onNavigateToLogin = onNavigateToLogin
                )
            }
        }
    }
}

// ==========================================
// 4. HOME TAB
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: BreadSweetViewModel,
    onNavigateToProductDetail: (Long) -> Unit,
    onSwitchToCategoryTab: () -> Unit
) {
    val context = LocalContext.current
    var bannerIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3500)
            bannerIndex = (bannerIndex + 1) % BreadSweetData.PROMO_BANNERS.size
        }
    }

    val filteredProducts = viewModel.products.filter { p ->
        val matchSearch = viewModel.searchQuery.isBlank() ||
                p.name.contains(viewModel.searchQuery, ignoreCase = true) ||
                p.category.contains(viewModel.searchQuery, ignoreCase = true)
        val matchCategory = viewModel.activeCategory == "Semua" || p.category == viewModel.activeCategory
        matchSearch && matchCategory
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Amber700, Amber600)
                    )
                )
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Selamat datang 👋", color = Amber100, fontSize = 13.sp)
                        Text(
                            text = viewModel.currentUser?.name ?: "Tamu",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                            .size(40.dp)
                    ) {
                        Box {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifikasi", tint = Color.White)
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color.Red, CircleShape)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Box
                OutlinedTextField(
                    value = viewModel.searchQuery,
                    onValueChange = { viewModel.searchQuery = it },
                    placeholder = { Text("Cari roti favoritmu...", fontSize = 13.sp, color = Amber100.copy(alpha = 0.8f)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Amber500) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }
        }

        // Scrollable content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Amber50)
                .padding(horizontal = 20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Promo Banner Slider
            item {
                val banner = BreadSweetData.PROMO_BANNERS[bannerIndex]
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(banner.bgStart), Color(banner.bgEnd))
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .clickable { onSwitchToCategoryTab() }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Promo", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = banner.title,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = banner.subtitle,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = banner.emoji,
                            fontSize = 48.sp,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Dots indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    BreadSweetData.PROMO_BANNERS.forEachIndexed { idx, _ ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .size(
                                    width = if (idx == bannerIndex) 16.dp else 6.dp,
                                    height = 6.dp
                                )
                                .background(
                                    color = if (idx == bannerIndex) Amber600 else Amber100,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Categories horizontal scroller
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(BreadSweetData.CATEGORIES) { cat ->
                        val selected = viewModel.activeCategory == cat
                        Card(
                            modifier = Modifier.clickable { viewModel.activeCategory = cat },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selected) Amber600 else Color.White
                            ),
                            border = BorderStroke(1.dp, if (selected) Amber600 else Amber100)
                        ) {
                            Text(
                                text = cat,
                                color = if (selected) Color.White else Amber700,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (viewModel.activeCategory == "Semua") "Produk Populer" else viewModel.activeCategory,
                        color = Amber800,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${filteredProducts.size} produk",
                        color = Amber500,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (filteredProducts.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔍", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Produk tidak ditemukan", color = Amber800, fontSize = 14.sp)
                    }
                }
            } else {
                // Products list as grid items
                // Compose doesn't support grid layouts inside a LazyColumn directly,
                // so we can chunk the products list into pairs of two to render as single rows!
                val chunks = filteredProducts.chunked(2)
                items(chunks) { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { product ->
                            ProductCard(
                                product = product,
                                onProductClick = { onNavigateToProductDetail(product.id) },
                                onAddToCart = {
                                    viewModel.addToCart(product)
                                    Toast.makeText(context, "${product.name} ditambahkan!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Fill spacer if single item in last row
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onProductClick() }
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Badges
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (product.isNew) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF2E7D32), RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Baru", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    if (product.isPromo) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFC62828), RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Promo", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.category.uppercase(),
                    color = Amber500,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = product.name,
                    color = DarkGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = " ${product.rating} ",
                        color = Amber700,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "(${product.sold})",
                        color = MediumGray,
                        fontSize = 10.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Rp ${String.format("%,.0f", product.price).replace(",", ".")}",
                            color = Amber700,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (product.originalPrice != null) {
                            Text(
                                text = "Rp ${String.format("%,.0f", product.originalPrice).replace(",", ".")}",
                                color = MediumGray,
                                fontSize = 10.sp,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(Amber500, RoundedCornerShape(8.dp))
                            .clickable { onAddToCart() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add to cart",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 5. CATEGORIES TAB
// ==========================================
@Composable
fun CategoriesScreen(
    viewModel: BreadSweetViewModel,
    onNavigateToProductDetail: (Long) -> Unit
) {
    val context = LocalContext.current
    var activeCategoryIndex by remember { mutableStateOf(0) }
    val categoriesList = BreadSweetData.CATEGORIES.filter { it != "Semua" }

    val filteredProducts = viewModel.products.filter { p ->
        p.category == categoriesList[activeCategoryIndex]
    }

    Row(modifier = Modifier.fillMaxSize()) {
        // Vertical Tab List (Navigation Drawer style)
        NavigationRail(
            containerColor = Color.White,
            modifier = Modifier.width(100.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            categoriesList.forEachIndexed { index, category ->
                val selected = index == activeCategoryIndex
                NavigationRailItem(
                    selected = selected,
                    onClick = { activeCategoryIndex = index },
                    icon = {
                        Text(
                            text = when (category) {
                                "Sweet Bread" -> "🍞"
                                "Savory Bread" -> "🍕"
                                "Pastry & Croissant" -> "🥐"
                                "Cakes" -> "🍰"
                                else -> "🥯"
                            },
                            fontSize = 24.sp
                        )
                    },
                    label = {
                        Text(
                            text = category,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = Amber700,
                        selectedTextColor = Amber700,
                        unselectedIconColor = MediumGray,
                        unselectedTextColor = MediumGray,
                        indicatorColor = Amber100
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Divider(modifier = Modifier.fillMaxHeight().width(1.dp), color = BorderColor)

        // Products List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Amber50)
                .padding(16.dp)
        ) {
            Text(
                text = categoriesList[activeCategoryIndex],
                color = Amber800,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (filteredProducts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tidak ada produk di kategori ini", color = MediumGray, fontSize = 14.sp)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredProducts) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onNavigateToProductDetail(product.id) },
                            onAddToCart = {
                                viewModel.addToCart(product)
                                Toast.makeText(context, "${product.name} ditambahkan!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. CART TAB
// ==========================================
@Composable
fun CartScreen(
    viewModel: BreadSweetViewModel,
    onNavigateToCheckout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Amber50)
    ) {
        // Toolbar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Keranjang Belanja", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGray)
        }
        Divider(color = BorderColor)

        if (viewModel.cartItems.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("🛒", fontSize = 64.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Keranjang Anda kosong", color = Amber800, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tambahkan produk lezat dari menu untuk memesan.", color = MediumGray, fontSize = 13.sp, textAlign = TextAlign.Center)
            }
        } else {
            Column(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(12.dp)) }
                    items(viewModel.cartItems) { item ->
                        CartItemRow(
                            item = item,
                            onQtyIncrement = { viewModel.updateQuantity(item.cartId, item.quantity + 1) },
                            onQtyDecrement = { viewModel.updateQuantity(item.cartId, item.quantity - 1) },
                            onDeleteClick = { viewModel.removeFromCart(item.cartId) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item { Spacer(modifier = Modifier.height(12.dp)) }
                }

                // Summary and checkout button
                Card(
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Item", color = MediumGray, fontSize = 14.sp)
                            Text("${viewModel.totalCartItems} roti", color = DarkGray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Bayar", color = MediumGray, fontSize = 15.sp)
                            Text(
                                text = "Rp ${String.format("%,.0f", viewModel.totalCartPrice).replace(",", ".")}",
                                color = Amber700,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onNavigateToCheckout,
                            colors = ButtonDefaults.buttonColors(containerColor = Amber600),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("Pesan Sekarang", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onQtyIncrement: () -> Unit,
    onQtyDecrement: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = item.product.image,
                contentDescription = item.product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.product.name,
                            fontWeight = FontWeight.Bold,
                            color = DarkGray,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Rasa: ${item.selectedVariant}",
                            color = Amber700,
                            fontSize = 11.sp
                        )
                    }
                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RedText, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rp ${String.format("%,.0f", item.product.price).replace(",", ".")}",
                        color = Amber800,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(1.dp, BorderColor, RoundedCornerShape(6.dp))
                                .clickable { onQtyDecrement() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("-", color = DarkGray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = item.quantity.toString(),
                            fontWeight = FontWeight.Bold,
                            color = DarkGray,
                            fontSize = 13.sp
                        )
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(1.dp, BorderColor, RoundedCornerShape(6.dp))
                                .clickable { onQtyIncrement() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", color = DarkGray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 7. PROFILE TAB
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: BreadSweetViewModel,
    onNavigateToLogin: () -> Unit
) {
    val user = viewModel.currentUser ?: return
    var name by remember { mutableStateOf(user.name) }
    var phone by remember { mutableStateOf(user.phone ?: "") }
    var address by remember { mutableStateOf(user.address ?: "") }
    var isEditing by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchOrdersFromCloud()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Amber50)
    ) {
        // Header info
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Amber800, Amber700)
                    ),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(vertical = 24.dp, horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (user.name.isNotEmpty()) user.name.take(1).uppercase() else "P",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Amber700
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(user.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(user.email, color = Amber100, fontSize = 12.sp)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Editable Profile details
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Detail Profil", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 15.sp)
                            Text(
                                text = if (isEditing) "Simpan" else "Ubah",
                                color = Amber700,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable {
                                        if (isEditing) {
                                            viewModel.updateProfile(name, phone, address)
                                            Toast.makeText(context, "Profil diperbarui!", Toast.LENGTH_SHORT).show()
                                        }
                                        isEditing = !isEditing
                                    }
                                    .padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        if (isEditing) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Nama") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                label = { Text("No. Handphone") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = address,
                                onValueChange = { address = it },
                                label = { Text("Alamat Pengiriman") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                maxLines = 3
                            )
                        } else {
                            ProfileInfoItem(label = "Nama", value = user.name)
                            ProfileInfoItem(label = "No. Handphone", value = user.phone ?: "-")
                            ProfileInfoItem(label = "Alamat Pengiriman", value = user.address ?: "-")
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Order History Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Riwayat Pesanan",
                        fontWeight = FontWeight.Bold,
                        color = Amber800,
                        fontSize = 15.sp
                    )
                    IconButton(
                        onClick = { viewModel.fetchOrdersFromCloud() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        if (viewModel.isFetchingOrders) {
                            CircularProgressIndicator(color = Amber800, modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Amber800, modifier = Modifier.size(16.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Order History List
            val customerOrders = viewModel.orders.filter { it.customer == user.name }
            if (customerOrders.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Belum ada riwayat pesanan.",
                            color = MediumGray,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(customerOrders) { order ->
                    OrderHistoryItem(order = order)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Logout Button
            item {
                Button(
                    onClick = {
                        viewModel.logout()
                        onNavigateToLogin()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RedText)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Keluar dari Akun", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(label.uppercase(), color = MediumGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Text(value, color = DarkGray, fontSize = 14.sp, modifier = Modifier.padding(top = 2.dp))
    }
}

@Composable
fun OrderHistoryItem(order: Order) {
    val statusColor = when (order.status) {
        "pending" -> Pair(YellowBg, YellowText)
        "diproses" -> Pair(BlueBg, BlueText)
        else -> Pair(GreenBg, GreenText)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Order ID: #${order.id.takeLast(7)}", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 13.sp)
                    Text(text = "${order.time} · ${order.method}", color = MediumGray, fontSize = 10.sp)
                }

                Box(
                    modifier = Modifier
                        .background(statusColor.first, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = when (order.status) {
                            "pending" -> "Menunggu"
                            "diproses" -> "Diproses"
                            else -> "Selesai"
                        },
                        color = statusColor.second,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = order.items, color = MediumGray, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)

            Divider(color = BorderColor, modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total Tagihan", color = MediumGray, fontSize = 12.sp)
                Text(
                    text = "Rp ${String.format("%,.0f", order.total).replace(",", ".")}",
                    color = Amber700,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ==========================================
// 8. PRODUCT DETAIL SCREEN
// ==========================================
@Composable
fun ProductDetailScreen(
    productId: Long,
    viewModel: BreadSweetViewModel,
    onNavigateBack: () -> Unit
) {
    val product = viewModel.products.firstOrNull { it.id == productId } ?: return
    var selectedVariant by remember { mutableStateOf(product.variants.firstOrNull() ?: "Original") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp) // Bottom padding for Action Bar
        ) {
            // Header Image with Back Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Back Button overlay
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                        .size(40.dp)
                        .align(Alignment.TopStart)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = DarkGray)
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                // Category Tag
                Box(
                    modifier = Modifier
                        .background(Amber100, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(product.category.uppercase(), color = Amber700, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Product Title
                Text(product.name, color = DarkGray, fontSize = 24.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(6.dp))

                // Rating and Sold details
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Star", tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                    Text("  ${product.rating} ", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 13.sp)
                    Text(" ·  ${product.sold} Terjual", color = MediumGray, fontSize = 13.sp)
                }

                Divider(color = BorderColor, modifier = Modifier.padding(vertical = 16.dp))

                // Price display
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Harga", color = MediumGray, fontSize = 12.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Rp ${String.format("%,.0f", product.price).replace(",", ".")}",
                                color = Amber700,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (product.originalPrice != null) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Rp ${String.format("%,.0f", product.originalPrice).replace(",", ".")}",
                                    color = MediumGray,
                                    fontSize = 14.sp,
                                    textDecoration = TextDecoration.LineThrough
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .background(if (product.stock > 0) GreenBg else RedBg, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (product.stock > 0) "Tersedia: ${product.stock}" else "Habis",
                            color = if (product.stock > 0) GreenText else RedText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Divider(color = BorderColor, modifier = Modifier.padding(vertical = 16.dp))

                // Description
                Text("Deskripsi Roti", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 15.sp)
                Text(
                    text = product.description,
                    color = MediumGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Ingredients
                if (product.ingredients.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Bahan-bahan", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        product.ingredients.forEach { ingredient ->
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 3.dp)
                                    .background(LightGray, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(ingredient, color = DarkGray, fontSize = 12.sp)
                            }
                        }
                    }
                }

                Divider(color = BorderColor, modifier = Modifier.padding(vertical = 16.dp))

                // Variant Selector
                Text("Pilih Varian Rasa", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(product.variants) { variant ->
                        val selected = variant == selectedVariant
                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = if (selected) Amber600 else BorderColor,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .background(if (selected) Amber100.copy(alpha = 0.5f) else Color.White, RoundedCornerShape(10.dp))
                                .clickable { selectedVariant = variant }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(variant, color = if (selected) Amber700 else DarkGray, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Action bar bottom wrapper
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shadowElevation = 8.dp,
            color = Color.White
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = {
                        if (product.stock > 0) {
                            viewModel.addToCart(product, selectedVariant)
                            Toast.makeText(context, "${product.name} ($selectedVariant) ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        } else {
                            Toast.makeText(context, "Maaf, stok roti habis!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Amber600),
                    enabled = product.stock > 0
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart Icon")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tambah ke Keranjang", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Simple FlowRow helper representation in case of custom components
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        content = { content() }
    )
}

// ==========================================
// 9. CHECKOUT SCREEN
// ==========================================
@Composable
fun CheckoutScreen(
    viewModel: BreadSweetViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToMainWithCart: () -> Unit
) {
    val user = viewModel.currentUser ?: return
    var phone by remember { mutableStateOf(user.phone ?: "") }
    var address by remember { mutableStateOf(user.address ?: "") }
    var deliveryMethod by remember { mutableStateOf("Home Delivery") } // "Home Delivery", "Pick-up"
    var paymentMethod by remember { mutableStateOf("COD") } // "COD", "Transfer Bank", "E-Wallet"
    var isCheckingOut by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val shippingFee = if (deliveryMethod == "Home Delivery" && viewModel.totalCartPrice < 100000) 10000.0 else 0.0
    val totalBill = viewModel.totalCartPrice + shippingFee

    LaunchedEffect(isCheckingOut) {
        if (isCheckingOut) {
            delay(1000)
            val summary = viewModel.cartItems.joinToString(", ") { "${it.product.name} ×${it.quantity}" }
            viewModel.addOrder(user.name, summary, totalBill, deliveryMethod)
            viewModel.clearCart()
            isCheckingOut = false
            Toast.makeText(context, "Pesanan Anda berhasil dikirim!", Toast.LENGTH_LONG).show()
            onNavigateToMainWithCart()
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 12.dp, horizontal = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = DarkGray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pengiriman & Pembayaran", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGray)
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Amber50)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .padding(bottom = 90.dp) // extra padding for bottom pay now bar
            ) {
                // Section Info Kontak & Alamat
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Informasi Penerima", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("No. Handphone") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Alamat Lengkap") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            maxLines = 3
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Metode Pengiriman
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Metode Pengiriman", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Home Delivery Option
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .border(
                                        width = 1.dp,
                                        color = if (deliveryMethod == "Home Delivery") Amber600 else BorderColor,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .background(if (deliveryMethod == "Home Delivery") Amber100.copy(alpha = 0.4f) else Color.White, RoundedCornerShape(10.dp))
                                    .clickable { deliveryMethod = "Home Delivery" }
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("🚚", fontSize = 24.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Kirim ke Alamat", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGray)
                                    Text("Rp 10.000 (Free >100rb)", fontSize = 9.sp, color = MediumGray, textAlign = TextAlign.Center)
                                }
                            }

                            // Pick-up Option
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .border(
                                        width = 1.dp,
                                        color = if (deliveryMethod == "Pick-up") Amber600 else BorderColor,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .background(if (deliveryMethod == "Pick-up") Amber100.copy(alpha = 0.4f) else Color.White, RoundedCornerShape(10.dp))
                                    .clickable { deliveryMethod = "Pick-up" }
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("🏪", fontSize = 24.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Ambil di Toko", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGray)
                                    Text("Gratis Biaya", fontSize = 9.sp, color = MediumGray, textAlign = TextAlign.Center)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Metode Pembayaran
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Metode Pembayaran", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        listOf("COD (Bayar di Tempat)", "Transfer Bank", "E-Wallet (OVO / GoPay)").forEach { method ->
                            val actualVal = when {
                                method.startsWith("COD") -> "COD"
                                method.startsWith("Transfer") -> "Transfer Bank"
                                else -> "E-Wallet"
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { paymentMethod = actualVal }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = paymentMethod == actualVal,
                                    onClick = { paymentMethod = actualVal },
                                    colors = RadioButtonDefaults.colors(selectedColor = Amber600)
                                )
                                Text(text = method, color = DarkGray, fontSize = 14.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Rincian Pembayaran
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Rincian Biaya", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal", color = MediumGray, fontSize = 13.sp)
                            Text("Rp ${String.format("%,.0f", viewModel.totalCartPrice).replace(",", ".")}", color = DarkGray, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Ongkos Kirim", color = MediumGray, fontSize = 13.sp)
                            Text(
                                text = if (shippingFee > 0) "Rp ${String.format("%,.0f", shippingFee).replace(",", ".")}" else "Gratis",
                                color = if (shippingFee > 0) DarkGray else GreenText,
                                fontSize = 13.sp
                            )
                        }

                        Divider(color = BorderColor, modifier = Modifier.padding(vertical = 12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Pembayaran", color = DarkGray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Rp ${String.format("%,.0f", totalBill).replace(",", ".")}",
                                color = Amber700,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Pay Now Button Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            if (phone.isBlank() || address.isBlank()) {
                                Toast.makeText(context, "Lengkapi nomor hp dan alamat Anda", Toast.LENGTH_SHORT).show()
                            } else {
                                isCheckingOut = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Amber600),
                        enabled = !isCheckingOut
                    ) {
                        if (isCheckingOut) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Bayar Sekarang", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 10. ADMIN DASHBOARD SCREEN
// ==========================================
@Composable
fun AdminDashboardScreen(
    viewModel: BreadSweetViewModel,
    onNavigateToLogin: () -> Unit
) {
    var activeTab by remember { mutableStateOf(0) } // 0: Dashboard, 1: Produk, 2: Pesanan
    val user = viewModel.currentUser ?: return
    val context = LocalContext.current

    LaunchedEffect(activeTab) {
        viewModel.fetchOrdersFromCloud()
    }

    // Admin Add/Edit states
    var showProductDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<Product?>(null) }
    var formName by remember { mutableStateOf("") }
    var formCategory by remember { mutableStateOf("") }
    var formVariants by remember { mutableStateOf("") }
    var formPrice by remember { mutableStateOf("") }
    var formStock by remember { mutableStateOf("") }
    var formImage by remember { mutableStateOf("") }

    // Admin filters
    var orderSearchQuery by remember { mutableStateOf("") }
    var orderStatusFilter by remember { mutableStateOf("semua") } // "semua", "pending", "diproses", "selesai"

    val pendingCount = viewModel.orders.count { it.status == "pending" }
    val diprosesCount = viewModel.orders.count { it.status == "diproses" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray)
    ) {
        // Toolbar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Amber800, Amber700)
                    )
                )
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🛡️", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("ADMIN PANEL", color = Amber100, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("BreadSweet", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(user.name, color = Amber100, fontSize = 12.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.fetchOrdersFromCloud() },
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                .size(36.dp)
                        ) {
                            if (viewModel.isFetchingOrders) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                viewModel.logout()
                                onNavigateToLogin()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f)),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Keluar", color = Color.White, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tab selection row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    val tabs = listOf("Dashboard", "Produk", "Pesanan")
                    tabs.forEachIndexed { index, title ->
                        val selected = activeTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(if (selected) Color.White else Color.Transparent, RoundedCornerShape(8.dp))
                                .clickable { activeTab = index }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                color = if (selected) Amber800 else Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Sub Screen contents
        Box(modifier = Modifier.weight(1f)) {
            when (activeTab) {
                0 -> {
                    // --- Admin Dashboard Tab ---
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        item {
                            Text("Ringkasan Hari Ini", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        item {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                // Revenue summary card
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("🏆", fontSize = 24.sp)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text("Pendapatan", color = MediumGray, fontSize = 11.sp)
                                        Text("Rp 499.000", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 16.sp)
                                    }
                                }

                                // Total orders summary card
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("📋", fontSize = 24.sp)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text("Total Pesanan", color = MediumGray, fontSize = 11.sp)
                                        Text("${viewModel.orders.size} Pesanan", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 16.sp)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        item {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                // Pending count card
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = YellowBg),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("⏳", fontSize = 24.sp)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text("Menunggu", color = YellowText, fontSize = 11.sp)
                                        Text("$pendingCount Pesanan", fontWeight = FontWeight.Bold, color = YellowText, fontSize = 16.sp)
                                    }
                                }

                                // Processing count card
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = BlueBg),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("⚙️", fontSize = 24.sp)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text("Diproses", color = BlueText, fontSize = 11.sp)
                                        Text("$diprosesCount Pesanan", fontWeight = FontWeight.Bold, color = BlueText, fontSize = 16.sp)
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                            Text("Aksi Cepat", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(10.dp))

                            // Fast actions buttons
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        editingProduct = null
                                        formName = ""
                                        formCategory = BreadSweetData.CATEGORIES.filter { it != "Semua" }[0]
                                        formVariants = ""
                                        formPrice = ""
                                        formStock = ""
                                        formImage = ""
                                        showProductDialog = true
                                    },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Amber600)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("➕", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Tambah Produk Baru", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("Input detail roti dan varian rasa", color = Amber100, fontSize = 11.sp)
                                    }
                                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Arrow", tint = Color.White)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { activeTab = 2 },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, BorderColor)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("📦", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Lihat Pesanan Aktif", color = DarkGray, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("Proses atau selesaikan order", color = MediumGray, fontSize = 11.sp)
                                    }
                                    if (pendingCount > 0) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFFFFB300), RoundedCornerShape(12.dp))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text("$pendingCount Baru", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Arrow", tint = DarkGray)
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                            Text("Katalog Terlaris", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        items(viewModel.products.take(3)) { product ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                border = BorderStroke(1.dp, BorderColor)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = product.image,
                                        contentDescription = product.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(45.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(product.name, fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 13.sp)
                                        Text("${product.sold} Terjual", color = MediumGray, fontSize = 11.sp)
                                    }
                                    Text(
                                        text = "Rp ${String.format("%,.0f", product.price).replace(",", ".")}",
                                        color = Amber700,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // --- Admin Product Tab ---
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Manajemen Produk (${viewModel.products.size})",
                                fontWeight = FontWeight.Bold,
                                color = DarkGray,
                                fontSize = 15.sp
                            )
                            Button(
                                onClick = {
                                    editingProduct = null
                                    formName = ""
                                    formCategory = BreadSweetData.CATEGORIES.filter { it != "Semua" }[0]
                                    formVariants = ""
                                    formPrice = ""
                                    formStock = ""
                                    formImage = ""
                                    showProductDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Amber600),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Tambah Roti", fontSize = 12.sp)
                            }
                        }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(viewModel.products) { product ->
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(1.dp, BorderColor),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(modifier = Modifier.padding(12.dp)) {
                                        AsyncImage(
                                            model = product.image,
                                            contentDescription = product.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(65.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.Top
                                            ) {
                                                Text(
                                                    product.name,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DarkGray,
                                                    fontSize = 14.sp,
                                                    modifier = Modifier.weight(1f),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .background(
                                                            if (product.stock > 10) GreenBg else RedBg,
                                                            RoundedCornerShape(8.dp)
                                                        )
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        "Stok: ${product.stock}",
                                                        color = if (product.stock > 10) GreenText else RedText,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }

                                            Text(
                                                text = "${product.category} · ${product.variants.joinToString(", ")}",
                                                color = MediumGray,
                                                fontSize = 11.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Rp ${String.format("%,.0f", product.price).replace(",", ".")}",
                                                    color = Amber800,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                    // Edit button
                                                    OutlinedButton(
                                                        onClick = {
                                                            editingProduct = product
                                                            formName = product.name
                                                            formCategory = product.category
                                                            formVariants = product.variants.joinToString(", ")
                                                            formPrice = product.price.toInt().toString()
                                                            formStock = product.stock.toString()
                                                            formImage = product.image
                                                            showProductDialog = true
                                                        },
                                                        shape = RoundedCornerShape(8.dp),
                                                        border = BorderStroke(1.dp, Amber600),
                                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                        modifier = Modifier.height(28.dp)
                                                    ) {
                                                        Text("Ubah", fontSize = 10.sp, color = Amber700)
                                                    }

                                                    // Delete button
                                                    OutlinedButton(
                                                        onClick = {
                                                            viewModel.deleteProduct(product.id)
                                                            Toast.makeText(context, "Produk ${product.name} dihapus!", Toast.LENGTH_SHORT).show()
                                                        },
                                                        shape = RoundedCornerShape(8.dp),
                                                        border = BorderStroke(1.dp, Color.Red),
                                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                        modifier = Modifier.height(28.dp)
                                                    ) {
                                                        Text("Hapus", fontSize = 10.sp, color = Color.Red)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // --- Admin Orders Tab ---
                    Column(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Pesanan Aktif", fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(10.dp))

                            // Search bar
                            OutlinedTextField(
                                value = orderSearchQuery,
                                onValueChange = { orderSearchQuery = it },
                                placeholder = { Text("Cari nama pelanggan...", fontSize = 13.sp) },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = MediumGray) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Horizontal Status filters row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("semua", "pending", "diproses", "selesai").forEach { status ->
                                    val count = if (status == "semua") viewModel.orders.size else viewModel.orders.count { it.status == status }
                                    val selected = orderStatusFilter == status
                                    Card(
                                        modifier = Modifier.clickable { orderStatusFilter = status },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (selected) Amber600 else Color.White
                                        ),
                                        border = BorderStroke(1.dp, if (selected) Amber600 else BorderColor)
                                    ) {
                                        Text(
                                            text = "${if (status == "semua") "Semua" else if (status == "pending") "Menunggu" else if (status == "diproses") "Diproses" else "Selesai"} ($count)",
                                            color = if (selected) Color.White else DarkGray,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }

                        val filteredOrders = viewModel.orders.filter { o ->
                            val matchSearch = orderSearchQuery.isBlank() || o.customer.contains(orderSearchQuery, ignoreCase = true) || o.id.contains(orderSearchQuery)
                            val matchStatus = orderStatusFilter == "semua" || o.status == orderStatusFilter
                            matchSearch && matchStatus
                        }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (filteredOrders.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 40.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Tidak ada pesanan ditemukan", color = MediumGray, fontSize = 14.sp)
                                    }
                                }
                            } else {
                                items(filteredOrders) { order ->
                                    val statusColor = when (order.status) {
                                        "pending" -> Pair(YellowBg, YellowText)
                                        "diproses" -> Pair(BlueBg, BlueText)
                                        else -> Pair(GreenBg, GreenText)
                                    }

                                    Card(
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        border = BorderStroke(1.dp, BorderColor),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {
                                                    Text(order.customer, fontWeight = FontWeight.Bold, color = DarkGray, fontSize = 14.sp)
                                                    Text("ID: #${order.id.takeLast(7)} · ${order.time} · ${order.method}", color = MediumGray, fontSize = 10.sp)
                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .background(statusColor.first, RoundedCornerShape(8.dp))
                                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        text = when (order.status) {
                                                            "pending" -> "Menunggu"
                                                            "diproses" -> "Diproses"
                                                            else -> "Selesai"
                                                        },
                                                        color = statusColor.second,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(order.items, color = MediumGray, fontSize = 12.sp)

                                            Spacer(modifier = Modifier.height(10.dp))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Rp ${String.format("%,.0f", order.total).replace(",", ".")}",
                                                    color = Amber700,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                when (order.status) {
                                                    "pending" -> {
                                                        Button(
                                                            onClick = {
                                                                viewModel.updateOrderStatus(order.id, "diproses")
                                                                Toast.makeText(context, "Pesanan mulai diproses!", Toast.LENGTH_SHORT).show()
                                                            },
                                                            colors = ButtonDefaults.buttonColors(containerColor = BlueText),
                                                            shape = RoundedCornerShape(8.dp),
                                                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                                                            modifier = Modifier.height(30.dp)
                                                        ) {
                                                            Text("Proses", fontSize = 11.sp)
                                                        }
                                                    }
                                                    "diproses" -> {
                                                        Button(
                                                            onClick = {
                                                                viewModel.updateOrderStatus(order.id, "selesai")
                                                                Toast.makeText(context, "Pesanan selesai dikirim!", Toast.LENGTH_SHORT).show()
                                                            },
                                                            colors = ButtonDefaults.buttonColors(containerColor = GreenText),
                                                            shape = RoundedCornerShape(8.dp),
                                                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                                                            modifier = Modifier.height(30.dp)
                                                        ) {
                                                            Text("Selesai", fontSize = 11.sp)
                                                        }
                                                    }
                                                    "selesai" -> {
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                        ) {
                                                            Icon(Icons.Default.Check, contentDescription = "Selesai", tint = GreenText, modifier = Modifier.size(16.dp))
                                                            Text("Selesai", color = GreenText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            item { Spacer(modifier = Modifier.height(16.dp)) }
                        }
                    }
                }
            }
        }
    }

    // Modal Add/Edit product dialog
    if (showProductDialog) {
        Dialog(onDismissRequest = { showProductDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = if (editingProduct == null) "Tambah Roti Baru" else "Edit Roti",
                        fontWeight = FontWeight.Bold,
                        color = DarkGray,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = formName,
                        onValueChange = { formName = it },
                        label = { Text("Nama Roti *") },
                        placeholder = { Text("cth. Roti Keju") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Simplified dropdown for categories
                    OutlinedTextField(
                        value = formCategory,
                        onValueChange = { formCategory = it },
                        label = { Text("Kategori (Sweet Bread / Savory Bread / Pastry & Croissant / Cakes) *") },
                        placeholder = { Text("Sweet Bread") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = formVariants,
                        onValueChange = { formVariants = it },
                        label = { Text("Varian (koma terpisah)") },
                        placeholder = { Text("Original, Cokelat") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = formPrice,
                        onValueChange = { formPrice = it },
                        label = { Text("Harga (Rp) *") },
                        placeholder = { Text("15000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = formStock,
                        onValueChange = { formStock = it },
                        label = { Text("Stok *") },
                        placeholder = { Text("20") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = formImage,
                        onValueChange = { formImage = it },
                        label = { Text("Link Image URL") },
                        placeholder = { Text("http://...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showProductDialog = false },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Batal")
                        }

                        Button(
                            onClick = {
                                val priceVal = formPrice.toDoubleOrNull() ?: 0.0
                                val stockVal = formStock.toIntOrNull() ?: 0
                                if (formName.isBlank() || formCategory.isBlank() || priceVal <= 0.0) {
                                    Toast.makeText(context, "Mohon lengkapi data yang berbintang", Toast.LENGTH_SHORT).show()
                                } else {
                                    val variantList = formVariants.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                                    if (editingProduct == null) {
                                        viewModel.addProduct(
                                            formName,
                                            formCategory,
                                            variantList,
                                            priceVal,
                                            formImage,
                                            "",
                                            stockVal
                                        )
                                        Toast.makeText(context, "Produk berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        viewModel.editProduct(
                                            editingProduct!!.id,
                                            formName,
                                            formCategory,
                                            variantList,
                                            priceVal,
                                            formImage,
                                            editingProduct!!.description,
                                            stockVal
                                        )
                                        Toast.makeText(context, "Produk berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                                    }
                                    showProductDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Amber600),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Simpan")
                        }
                    }
                }
            }
        }
    }
}
