package com.example.ton.whattheeat.model

import java.util.*
import kotlin.collections.ArrayList

abstract class FoodRepository:Observable() {

    protected val foodList = ArrayList<Food>()
     var food = Food()

    abstract fun loadAllFood()
    abstract fun getFoods():ArrayList<Food>
    fun getFood(){
        println("Get foodd")
        var rand = Random()
        food = foodList[(rand.nextInt(foodList.size))]
        setChanged()
        notifyObservers()
    }
    fun getFood(withSelectedBar:String){

    }


}