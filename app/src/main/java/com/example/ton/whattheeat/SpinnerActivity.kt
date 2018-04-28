package com.example.ton.whattheeat

import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Toast
import com.example.ton.whattheeat.model.Food
import com.example.ton.whattheeat.model.FoodRepository
import com.example.ton.whattheeat.model.MockFoodRepository
import com.example.ton.whattheeat.presenter.FoodPresenter
import com.example.ton.whattheeat.presenter.FoodView
import kotlinx.android.synthetic.main.activity_spinner.*
import java.io.InputStream
import java.net.URL

class SpinnerActivity : AppCompatActivity(), FoodView {

    private lateinit var foodPresenter:FoodPresenter
    private lateinit var foodRepo:FoodRepository

    override fun setFood(food: Food) {
        try {
            println(food.imageUrl)
            val inputStream = URL(food.imageUrl).content as InputStream
        println(inputStream)
            food_image_id.setImageDrawable(Drawable.createFromStream(inputStream,"food image"))
        }
        catch (e : Exception){
            println("image not found")
        }
        food_name_id.text = food.foodName
        food_type_id.text = food.foodType
        food_price_id.text = food.price.toString()
        food_location_id.text = food.place
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        foodRepo = MockFoodRepository()
        foodPresenter = FoodPresenter(this,foodRepo)
        foodPresenter.start()
    }

    fun onGetFoodButtonClicked(view:View){
        foodPresenter.getFood()
    }
}
