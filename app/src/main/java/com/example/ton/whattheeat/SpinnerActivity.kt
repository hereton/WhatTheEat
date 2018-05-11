package com.example.ton.whattheeat

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.ton.whattheeat.model.Food
import com.example.ton.whattheeat.model.FoodRepository
import com.example.ton.whattheeat.model.MockFoodRepository
import com.example.ton.whattheeat.presenter.FoodPresenter
import com.example.ton.whattheeat.presenter.FoodView
import kotlinx.android.synthetic.main.activity_spinner.*
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.ObjectInputStream
import java.net.URL

class SpinnerActivity : AppCompatActivity(), FoodView {

    private lateinit var foodPresenter:FoodPresenter
    private lateinit var foodRepo:FoodRepository
    private lateinit var map:HashMap<String, ArrayList<String>>

    override fun setFood(food: Food) {
        val task = ImageLoader(food)
        task.execute()
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
        map = HashMap()
        loadDataFromLocalStorage()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.spinnermenu, menu)
        println("creating")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_home -> {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            true
        }


        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }


    fun onGetFoodButtonClicked(view:View){
        loadDataFromLocalStorage()
        if(can_checkBox_id.isChecked){
            println("Checked")
            if(map["foodAllergy"]!!.size != 0 ) {
                foodPresenter.getFood(map["foodAllergy"] as ArrayList<String>)
            }else{
                foodPresenter.getFood()
            }
        }else{
            println("UnChecked")
            foodPresenter.getFood()
        }
    }

    private fun loadDataFromLocalStorage(){
        try{
            val fis = this.openFileInput("whatTheEat")
            val ois = ObjectInputStream(fis)
            map = ois.readObject() as HashMap<String, ArrayList<String>>
        }
        catch (e:FileNotFoundException){
            println("File not found")
        }
        catch (e:Exception){
            println("something")
        }
    }
    @SuppressLint("StaticFieldLeak")
    inner class ImageLoader(private val food:Food):AsyncTask<String,String,InputStream>(){
        override fun doInBackground(vararg p0: String?): InputStream {
            return URL(food.imageUrl).content as InputStream
        }
        override  fun onPostExecute(result:InputStream){
            food_image_id.setImageDrawable(Drawable.createFromStream(result,"food image"))
        }

    }
}
