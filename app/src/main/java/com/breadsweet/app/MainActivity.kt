package com.breadsweet.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.breadsweet.app.ui.screens.*
import com.breadsweet.app.ui.theme.BreadSweetTheme
import com.breadsweet.app.viewmodel.BreadSweetViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: BreadSweetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BreadSweetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        composable("login") {
                            LoginScreen(
                                viewModel = viewModel,
                                onNavigateToHome = {
                                    navController.navigate("main/0") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToAdmin = {
                                    navController.navigate("admin_dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = {
                                    navController.navigate("register")
                                }
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                viewModel = viewModel,
                                onNavigateToHome = {
                                    navController.navigate("main/0") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToLogin = {
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(
                            route = "main/{tabIndex}",
                            arguments = listOf(navArgument("tabIndex") { type = NavType.IntType; defaultValue = 0 })
                        ) { backStackEntry ->
                            val tabIndex = backStackEntry.arguments?.getInt("tabIndex") ?: 0
                            MainScreen(
                                viewModel = viewModel,
                                initialTab = tabIndex,
                                onNavigateToProductDetail = { productId ->
                                    navController.navigate("product_detail/$productId")
                                },
                                onNavigateToCheckout = {
                                    navController.navigate("checkout")
                                },
                                onNavigateToLogin = {
                                    navController.navigate("login") {
                                        popUpTo("main/0") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(
                            route = "product_detail/{productId}",
                            arguments = listOf(navArgument("productId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
                            ProductDetailScreen(
                                productId = productId,
                                viewModel = viewModel,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("checkout") {
                            CheckoutScreen(
                                viewModel = viewModel,
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                onNavigateToMainWithCart = {
                                    // Navigate to Profile Tab (index 3) to show order details immediately
                                    navController.navigate("main/3") {
                                        popUpTo("main/0") { inclusive = false }
                                    }
                                }
                            )
                        }

                        composable("admin_dashboard") {
                            AdminDashboardScreen(
                                viewModel = viewModel,
                                onNavigateToLogin = {
                                    navController.navigate("login") {
                                        popUpTo("admin_dashboard") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
