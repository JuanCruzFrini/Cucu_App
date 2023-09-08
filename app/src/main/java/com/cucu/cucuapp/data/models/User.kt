package com.cucu.cucuapp.data.models

data class User(
    val id:String?="",
    val name:String?="",
    val img:String?="",
    val favorites:List<Product>? = emptyList(),
    val purchases:String?="",
    val cart:List<Product>? = emptyList(),
    val history:List<Product>? = emptyList(),
)
