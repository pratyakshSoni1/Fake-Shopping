package com.example.fakeshopping.ui.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.model.ProductsDetailScreenViewModel
import com.example.fakeshopping.ui.presentation.components.LoadingView
import com.example.fakeshopping.ui.theme.ColorYellow
import com.example.fakeshopping.utils.Routes

@Composable
fun ProductDetailScreen(navController: NavController, productId: Int, currentUser:String) {

    val viewModel: ProductsDetailScreenViewModel = hiltViewModel()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.setProductAndUserId(productId, currentUser)
    }

    if (viewModel.product.value == null) {
        LoadingView(modifier = Modifier.fillMaxSize(), circleSize = 64.dp)
    } else {

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {

            Column(
                modifier = Modifier.verticalScroll(state = rememberScrollState(), enabled = true)
            ) {

//                Spacer(Modifier.height(12.dp))

                //Product Preview
                ProductDetailsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 180.dp),
                    product = viewModel.product.value!!,
                    currentImageIndex = viewModel.currentProductPreviewSlide,
                    onBackArrowPress = {
                        navController.popBackStack()
                    },
                    isFavourite = viewModel.isFavouriteProduct,
                    onToggleFavouriteBtn = {
                        if (viewModel.userFavs.contains(productId)) {
                            viewModel.removeFromFavourites(productId)
                        }else{
                            viewModel.addProductToFavourites(productId)
                        }
                    }
                )

                Spacer(Modifier.height(24.dp))

                ActionButtons(
                    onAddToCart = {
                        viewModel.addToCart()
                        Toast.makeText(context, "Added To Cart ✔", Toast.LENGTH_SHORT).show()
                    },
                    onBuyBow = {
                        val itemsToBuyList = mutableListOf(viewModel.product.value!!.id)
                        val itemsQuantityList = mutableListOf(1)
                        navController.navigate("${Routes.checkOutOverviewScreen}/${itemsToBuyList.toString()}/${itemsQuantityList.toString()}")
                    }
                 )

                Spacer(Modifier.height(24.dp))

                //Recommendations
                ProductRecommendationsSection(
                    relevantProductList = viewModel.relevantproduct,
                    otherProductsList = viewModel.otherPproducts,
                    onNaviagte = {
                        navController.navigate(Routes.productDetailScreen + "/${it.id}")
                    },
                    onRelevantSeeAllBtnClick = {
                        navController.navigate("${Routes.homeScreen}?category=${viewModel.product.value!!.category}")
                    },
                    onOtherSeeAllBtnClick = {
                        navController.navigate("${Routes.homeScreen}?category=All")
                    },
                    onTogglefavButton = {
                        if (viewModel.userFavs.contains(it)) {
                            viewModel.removeFromFavourites(it)
                        }else{
                            viewModel.addProductToFavourites(it)
                        }
                    },
                    checkIsFavourite = {
                        viewModel.userFavs.contains(it)
                    }

                )

                Spacer(Modifier.height(120.dp))

            }


        }
    }

}

@Composable
fun ActionButtons( onAddToCart:()->Unit, onBuyBow:()->Unit ){

    Button(
        onClick = { onBuyBow() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ColorYellow
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.elevation(0.dp)
    ) {
        Text(
            "BUY NOW",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }

    Spacer(Modifier.height(6.dp))

    Button(
        onClick = { onAddToCart() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(3.dp, ColorYellow),
        elevation = ButtonDefaults.elevation(pressedElevation = 0.4.dp, defaultElevation = 0.dp, focusedElevation = 0.dp)
    ) {
        Text(
            "ADD TO CART",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }

}


@Composable
fun ProductDetailsSection(
    modifier: Modifier,
    product: ShopApiProductsResponse,
    currentImageIndex: MutableState<Int>,
    onBackArrowPress:()->Unit,
    isFavourite: State<Boolean?>,
    onToggleFavouriteBtn:()->Unit
) {

    val currentProductPrevSlideState = rememberLazyListState()
    val productImagesUrl = arrayOf(product.image, product.image)

    Column(modifier = modifier) {

        ProductPreviewSection(
            modifier = Modifier
                .fillMaxWidth()
                .height(243.dp),
            currentImageIndex = currentImageIndex,
            listState = currentProductPrevSlideState,
            onBackArrowPress= onBackArrowPress,
            product.image,product.image,
            isFavourite = isFavourite,
            ontoggleFavouriteBtn = { onToggleFavouriteBtn() }
        )

        Spacer(Modifier.height(18.dp))

        ProductTextDetails(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            product = product
        )

    }

    LaunchedEffect(key1 = currentImageIndex.value) {

        if (currentImageIndex.value < (productImagesUrl.size) && currentImageIndex.value >= 0) {
            currentProductPrevSlideState.animateScrollToItem(currentImageIndex.value)
            Log.i("SWIPE", "Scrolled to: ${currentImageIndex.value}")
        } else {
            Log.i("SWIPE", "ActionUp: NONE CONDITION MET !")
        }

    }


}

@Composable
fun ProductRecommendationsSection(
    relevantProductList: SnapshotStateList<ShopApiProductsResponse>,
    otherProductsList:SnapshotStateList<ShopApiProductsResponse>,
    onNaviagte:(ShopApiProductsResponse)->Unit,
    onRelevantSeeAllBtnClick: () -> Unit,
    onOtherSeeAllBtnClick: () -> Unit,
    checkIsFavourite:(Int)->Boolean,
    onTogglefavButton:(Int)->Unit
){

    Column(modifier= Modifier
        .fillMaxWidth()
        .wrapContentHeight()
    ){

            RelevantProductRecommendations(
                productsList = relevantProductList,
                onNavigate = onNaviagte,
                onSeeAllBtnClick = onRelevantSeeAllBtnClick,
                checkIsFavourite= checkIsFavourite,
                onTogglefavButton= onTogglefavButton
            )


            Spacer(Modifier.height(24.dp))

            OtherProductRecommendations(
                productsList = otherProductsList,
                onNavigate = onNaviagte,
                onOtherSeeAllBtnClick = onOtherSeeAllBtnClick,
                checkIsFavourite= checkIsFavourite,
                onTogglefavButton= onTogglefavButton
            )

            Spacer(Modifier.height(6.dp))


    }

}