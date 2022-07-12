package com.example.fakeshopping

import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fakeshopping.ui.presentation.HomeScreen
import com.example.fakeshopping.ui.presentation.HomeScreen_V2
import com.example.fakeshopping.ui.presentation.ProductDetailScreen
import com.example.fakeshopping.utils.Routes

@Composable
fun Navigation(window: Window) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.homeScreen) {

        composable(Routes.homeScreen) {
            window.statusBarColor = Color.Blue.toArgb()
            HomeScreen_V2(navController)
        }

        composable(
            route = Routes.productDetailScreen + "/{productId}",
            arguments = kotlin.collections.listOf(
                navArgument("productId") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            window.statusBarColor = Color.White.toArgb()
            ProductDetailScreen(navController, backStackEntry.arguments?.getInt("productId")!!)

        }

    }

}