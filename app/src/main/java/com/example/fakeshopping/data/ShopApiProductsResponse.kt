package com.example.fakeshopping.data

data class ShopApiProductsResponse(

    val id: Int,
    val title: String,
    val price: String,
    val category: String,
    val description: String,
    val image: String,
    val rating:ProductRating,

){

    data class ProductRating(
        val rate: Float,
        val count: Int
    )

}

