package com.example.ton.whattheeat.model

import java.util.*
import kotlin.collections.ArrayList

abstract class FoodRepository:Observable() {

    protected val foodList = ArrayList<Food>()
     var food = Food()
    private var rand = Random()

    abstract fun loadAllFood()
    abstract fun getFoods():ArrayList<Food>
    fun getFood(){
        println("Get food")
        food = foodList[(rand.nextInt(foodList.size))]
        setChanged()
        notifyObservers()
    }
    fun getFood(myAllergyFood:ArrayList<String>){
        println("get food allergy")
        var tempFoodList = ArrayList<Food>()
        myAllergyFood.forEach { type
            -> tempFoodList.addAll(foodList.filter {
            food
            -> food.foodType != type })
        }
        food = tempFoodList[(rand.nextInt(tempFoodList.size))]
        setChanged()
        notifyObservers()
    }


}