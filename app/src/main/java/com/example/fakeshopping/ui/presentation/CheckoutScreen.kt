package com.example.fakeshopping.ui.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.model.CheckoutScreenViewModel
import com.example.fakeshopping.ui.theme.ColorYellow
import com.example.fakeshopping.utils.PaymentOptionId
import com.example.fakeshopping.utils.PaymentScreenRoutes

@Composable
fun ProductCheckoutScreen( navController:NavHostController, currentUser:String, onContinueTOPayment:(paymentOptionRoute:String, amountToBePaid:Float)->Unit) {

    val viewModel: CheckoutScreenViewModel = hiltViewModel()

    LaunchedEffect(key1 = true, block = {
        viewModel.setCurrentUser(currentUser)
    })


    Scaffold(
        topBar = { CheckoutTopBar(onBackArrowClick = { navController.popBackStack() }) },
        bottomBar = {
            CheckoutScreenBottomBar(
                onProceedClick = {
                    val paymentRoute = when (viewModel.paymentMethod.value) {
                        PaymentOptionId.OPTION_CARD -> PaymentScreenRoutes.cardFragment
                        PaymentOptionId.OPTION_POD -> PaymentScreenRoutes.cardFragment
                        PaymentOptionId.OPTION_UPI -> PaymentScreenRoutes.upiFragment
                        PaymentOptionId.OPTOIN_NETBANKING -> PaymentScreenRoutes.netBankingFragment
                        PaymentOptionId.OPTOIN_WALLET -> PaymentScreenRoutes.walletFragment
                    }
                    onContinueTOPayment(paymentRoute, viewModel.totalCost.value)
                },
                onCancel = { navController.popBackStack() })
        },
        modifier = Modifier
            .statusBarsPadding()
    ) {

        val scrollState = rememberScrollState()
        Column(Modifier.verticalScroll(scrollState)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp), contentAlignment = Alignment.TopCenter
            ) {

                PriceDetailsCard(
                    numberOfItems = viewModel.cartItems.size,
                    itemsCost = viewModel.itemsCost.value,
                    totalTax = viewModel.tax.value,
                    totalDeliveryCharge = viewModel.deliveryCharge.value,
                    discount = viewModel.discount.value,
                    totalAmount = viewModel.totalCost.value
                )

            }

            Spacer(Modifier.height(36.dp))
            Text(
                "Choose Payment Method",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 22.dp)
            )

            Spacer(Modifier.height(16.dp))
            PaymentOptionsSelection(
                currentSelectedOption = viewModel.paymentMethod,
                onOptionChange = {
                    viewModel.changeCurrentPaymentMethod(it)
                })
            Spacer(Modifier.height(36.dp))

        }


    }

}

@Composable
private fun CheckoutTopBar( onBackArrowClick:()->Unit ){

    SmallTopAppBar(
        modifier = Modifier.padding(start= 14.dp, bottom=12.dp).background(Color.White),
        title = {
            Text(
                "Overview",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp,
                modifier=Modifier.padding(start= 14.dp, top=12.dp, bottom=12.dp)
            )
        },
        navigationIcon = {

            com.example.fakeshopping.ui.presentation.components.IconButton(
                icon =Icons.Default.ArrowBack ,
                onClick = { onBackArrowClick()  },
                contentDescription = "Go back"
            )

        },
    )


}


@Composable
private fun CheckoutScreenBottomBar( onProceedClick:()->Unit, onCancel:()->Unit ){

    Row(modifier= Modifier
        .fillMaxWidth(1f)
        .padding(vertical = 12.dp, horizontal = 12.dp)
        .background(Color.White),
        horizontalArrangement = Arrangement.Center
    ){

        Button(
            onClick = { onCancel() },
            modifier=Modifier.weight(0.16f),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent
            ),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
            ),
            border = BorderStroke(2.dp, ColorYellow)
        ) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(Modifier.fillMaxWidth(),contentAlignment = Alignment.CenterEnd){

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "cancel",
                    )
                }
            }
        }

        Spacer(Modifier.width(8.dp))

        Button(
            onClick = { onProceedClick() },
            modifier=Modifier.weight(0.8f),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorYellow
            )
        ) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Proceed", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Box(Modifier.fillMaxWidth(),contentAlignment = Alignment.CenterEnd){

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Proceed to Payment ?",
                    )
                }
            }
        }



    }

}

@Composable
private fun PriceDetailsCard( numberOfItems:Int, itemsCost:Float, totalTax:Float, totalDeliveryCharge:Float, discount:Float, totalAmount:Float ){

    Card(modifier= Modifier
        .fillMaxWidth(0.85f),
        shape=RoundedCornerShape(12.dp),
        elevation= 6.dp
    )
    {
        Column {
            Text(
                text = "Overview", modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 12.dp, start = 21.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Box(
                Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .background(Color.White), contentAlignment = Alignment.Center) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {

                    Text(
                        text = "$numberOfItems items",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(start = 10.dp)
                    )

                    OverviewCardItem(title = "Item's Cost", cost = itemsCost, true)
                    OverviewCardItem(title = "Discount", cost = discount, false)
                    OverviewCardItem(title = "Total Tax", cost = totalTax, true)
                    OverviewCardItem(title = "Delivery charge", cost = totalDeliveryCharge, true)

                }
            }

            Card(Modifier.fillMaxWidth(), elevation = 6.dp, shape= RoundedCornerShape( topStart = 0.dp, topEnd = 0.dp, bottomStart = 12.dp, bottomEnd =  12.dp)) {
                Column(
                    Modifier
                        .background(ColorYellow)
                ) {

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {

                        Text(
                            "Total",
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .padding(start = 16.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            "$$totalAmount",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 18.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )

                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

        }
    }
}

@Composable
fun OverviewCardItem(title:String, cost:Float, isAddingToAmount:Boolean){

    Row(
        modifier = Modifier
            .fillMaxWidth()

    ) {

        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(start = 10.dp)
        )
        Text(
            text = if(isAddingToAmount) "$$cost" else "- $$cost",
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            textAlign = TextAlign.End,
            color = if(isAddingToAmount) Color.Black else Color.Green
        )

    }

}

@Composable
fun PaymentOptionsSelection(currentSelectedOption: State<PaymentOptionId>, onOptionChange:(option:PaymentOptionId)->Unit){

    Column(modifier= Modifier
        .fillMaxWidth(0.85f),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Card(
            shape = RoundedCornerShape(12.dp),
            border = if(currentSelectedOption.value == PaymentOptionId.OPTION_CARD) BorderStroke(1.7.dp, Color(0xFF350099)) else null,
            modifier=Modifier.clickable { onOptionChange(PaymentOptionId.OPTION_CARD) }.fillMaxWidth().padding(horizontal= 12.dp)
        ) {
            Text("Card", modifier=Modifier.fillMaxWidth().padding(start= 18.dp, top=16.dp, bottom=16.dp), fontWeight = FontWeight.Bold )
        }

        Spacer(modifier=Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            border = if(currentSelectedOption.value == PaymentOptionId.OPTION_UPI) BorderStroke(1.7.dp, Color(0xFF350099)) else null,
            modifier=Modifier.fillMaxWidth().clickable { onOptionChange(PaymentOptionId.OPTION_UPI) }.padding(horizontal=12.dp)
        ) {
            Text("UPI", modifier=Modifier.fillMaxWidth().padding(start= 18.dp, top=16.dp, bottom=16.dp), fontWeight = FontWeight.Bold )
        }

        Spacer(modifier=Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            border = if(currentSelectedOption.value == PaymentOptionId.OPTOIN_NETBANKING) BorderStroke(1.7.dp, Color(0xFF350099)) else null,
            modifier=Modifier.fillMaxWidth().clickable { onOptionChange(PaymentOptionId.OPTOIN_NETBANKING) }.padding(start= 18.dp, top=16.dp, bottom=16.dp)
        ) {
            Text("Net Banking", modifier=Modifier.fillMaxWidth().padding(start= 14.dp, top=12.dp, bottom=12.dp), fontWeight = FontWeight.Bold )
        }

        Spacer(modifier=Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            border = if(currentSelectedOption.value == PaymentOptionId.OPTOIN_WALLET) BorderStroke(1.7.dp, Color(0xFF350099)) else null,
            modifier=Modifier.clickable { onOptionChange(PaymentOptionId.OPTOIN_WALLET) }.fillMaxWidth().padding(horizontal=12.dp)
        ) {
            Text("Wallet", modifier=Modifier.fillMaxWidth().padding(start= 18.dp, top=16.dp, bottom=16.dp), fontWeight = FontWeight.Bold )
        }

        Spacer(modifier=Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            border = if(currentSelectedOption.value == PaymentOptionId.OPTION_POD) BorderStroke(1.7.dp, Color(0xFF350099)) else null,
            modifier=Modifier.clickable { onOptionChange(PaymentOptionId.OPTION_POD) }.fillMaxWidth().padding(horizontal=12.dp)
        ) {
            Text("Pay On Delivery", modifier=Modifier.fillMaxWidth().padding(start= 18.dp, top=16.dp, bottom=16.dp), fontWeight = FontWeight.Bold )
        }

        Spacer(modifier=Modifier.height(8.dp))


    }

}