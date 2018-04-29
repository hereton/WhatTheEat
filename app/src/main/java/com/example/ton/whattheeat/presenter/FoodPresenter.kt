package com.example.ton.whattheeat.presenter

import com.example.ton.whattheeat.model.Food
import com.example.ton.whattheeat.model.FoodRepository
import java.util.*
import kotlin.collections.ArrayList

class FoodPresenter(private val view:FoodView, private val repository:FoodRepository):Observer {

    fun start(){
        repository.addObserver(this)
        repository.loadAllFood()
    }

    override fun update(obj: Observable?, p1: Any?) {
        if(obj == repository){
            view.setFood(repository.food)
        }
    }

    fun getFood(){
        repository.getFood()
    }
}