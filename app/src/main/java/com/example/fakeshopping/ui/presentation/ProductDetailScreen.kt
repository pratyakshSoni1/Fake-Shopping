package com.example.fakeshopping.ui.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.ProductsDetailScreenViewModel

@Composable
fun ProductDetailScreen(navController: NavController, productId: Int) {

    val viewModel: ProductsDetailScreenViewModel = hiltViewModel()

    LaunchedEffect(key1 = true) {
        viewModel.setProduct(productId)
    }

    if (viewModel.product.value == null) {
        Text("Loading", fontSize = 18.sp, color = Color.DarkGray)
    } else {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomBarContent(
                    modifier = Modifier
                )
            }
        ) {

            Column() {
                    //Product Preview
                    ProductDetailsSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 180.dp),
                        product = viewModel.product.value!!,
                        currentImageIndex = viewModel.currentProductPreviewSlide
                    )

                Spacer(Modifier.height(12.dp))

                //Recommendations
                ProductRecommendationsSection(
                    productCategory = viewModel.product.value!!.category,
                    relevantProductList = viewModel.relevantproduct,
                    otherProductsList = viewModel.otherPproducts,
                    onNaviagte = {
                        //HI
                    }
                )

            }


        }
    }

}

@Composable
fun ProductRecommendationsSection(productCategory:String, relevantProductList: SnapshotStateList<ShopApiProductsResponse>, otherProductsList:SnapshotStateList<ShopApiProductsResponse>, onNaviagte:(ShopApiProductsResponse)->Unit){

    Column(modifier=Modifier.fillMaxWidth()){


            RelevantProductRecommendations(
                productCategory = productCategory,
                productsList = relevantProductList,
                onNavigate = onNaviagte
            )


            Spacer(Modifier.height(6.dp))

            OtherProductRecommendations(
                productsList = otherProductsList,
                onNavigate = onNaviagte
            )


    }

}