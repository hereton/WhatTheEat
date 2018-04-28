package com.example.ton.whattheeat.model

import com.example.ton.whattheeat.model.Food
import com.example.ton.whattheeat.model.FoodRepository
import java.util.*
import kotlin.collections.ArrayList

class MockFoodRepository: FoodRepository() {

    override fun getFoods():ArrayList<Food> {
        return  foodList
    }

    override fun loadAllFood() {
        foodList.clear()
        foodList.add(Food("kapow gai kai dow","meat",35,"iup","https://pbs.twimg.com/profile_images/853289222374961156/3rH3zOtM_400x400.jpg"))
        foodList.add(Food("ka nom jeen nam ya","meat",35,"bar vit","https://food.mthai.com/app/uploads/2017/05/Noodles-with-coconut-milk.jpg"))
        foodList.add(Food("Fish Steak","seafood",40,"bar mai","https://comps.canstockphoto.com/fish-steak-grilled-vegetables-stock-photo_csp31903748.jpg"))
    }


}