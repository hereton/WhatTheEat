package com.example.ton.whattheeat.model

class Food(var foodName:String = "", var foodType:String = "", var price:Int = 0,
           var place:String = "",var imageUrl:String = "") {

    override fun toString():String{
        return "Food name $foodName, price: $price"
    }
}