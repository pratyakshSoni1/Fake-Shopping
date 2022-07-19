package com.example.fakeshopping.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun AccountDialog(menuItems:List<MenuItemData>, showDialogValue: MutableState<Boolean>){

    Dialog(onDismissRequest = { showDialogValue.value = false }) {

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            modifier = Modifier.defaultMinSize(minHeight = 350.dp)
        ) {

            Column() {

                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {

                    Row(
                        modifier = Modifier
                            .padding(start=47.dp,top = 24.dp, bottom= 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(63.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(text = "Username", fontSize = 14.sp, fontWeight = FontWeight.Bold)

                    }

                    Box(modifier=Modifier.matchParentSize().padding(end = 14.dp, top=14.dp, bottom = 8.dp),contentAlignment = Alignment.TopEnd){

                        Icon(
                            modifier = Modifier
                                .clickable {
                                    showDialogValue.value = false
                                }
                                .clip(CircleShape),
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Menu"
                        )

                    }

                }

                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )



                for (items in menuItems) {
                    AccountDialogitem(
                        icon = items.icon,
                        text = items.text,
                        onClick = items.onClickListener
                    )
                }


                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )
                Spacer(Modifier.height(14.dp))

            }

        }
    }
}

@Composable
fun AccountDialogitem(icon:ImageVector, text:String, onClick:() -> Unit){

    Row(modifier= Modifier
        .clickable { onClick() }
        .padding(top=12.dp,bottom=12.dp,start=21.dp)
        .fillMaxWidth()) {

        Icon(imageVector = icon, contentDescription = text)
        Spacer(Modifier.width(21.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Start
        )

    }

}