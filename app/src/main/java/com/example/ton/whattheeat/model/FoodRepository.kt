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
        val tempFoodList = ArrayList<Food>()
        foodList.forEach { type -> if(!myAllergyFood.contains(type.foodType)){
                tempFoodList.add(type)
            }
        }
        food = tempFoodList[(rand.nextInt(tempFoodList.size))]
        setChanged()
        notifyObservers()
    }


}