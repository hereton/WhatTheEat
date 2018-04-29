package com.example.ton.whattheeat

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ProfileActivity : AppCompatActivity() {

    private lateinit var foodAllergy : ArrayList<String>
    private lateinit var typeOfFoodList:ArrayList<String>
    private lateinit var map: HashMap<String,ArrayList<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        if(supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        typeOfFoodList = arrayListOf("meat","seafood")
        foodAllergy = ArrayList()
        map = hashMapOf()

        try {
            val fis = this.openFileInput("whatTheEat")
            val ois = ObjectInputStream(fis)
             map = ois.readObject() as HashMap<String, ArrayList<String>>
        }catch (e:FileNotFoundException){
            println("File not found")
        }catch (e:Exception){
            println("something")
        }
        if(map.isNotEmpty()){




            foodAllergy = map["foodAllergy"] as ArrayList<String>
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, foodAllergy)
            cant_listView_id.adapter = adapter

            name_editText_id.setText(map["name"].toString().replace("[","").replace("]",""))
        }


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    fun onImageHandleClick(view:View){
        println("clicked")
    }

    fun onAddButtonClick(view:View){
        val alert = AlertDialog.Builder(this)
        alert.setTitle("food allergy")
        alert.setMessage("select type of food")

        // Set an EditText view to get user input
        val spinner = Spinner(this)
        val spinnerAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,typeOfFoodList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        alert.setView(spinner)


        alert.setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ ->
            try {
                val foodType = spinner.selectedItem.toString()
                if(!foodAllergy.contains(foodType)){
                    foodAllergy.add(foodType)
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, foodAllergy)
                cant_listView_id.adapter = adapter
            }catch (e:NumberFormatException){
                Toast.makeText(this, "Please input number", Toast.LENGTH_SHORT).show()
            }finally {
                return@OnClickListener
            }
        })

        alert.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { _, _ ->
                    return@OnClickListener
                })
        alert.show()
    }

    fun onDeleteButtonClick(view:View){
        val alert = AlertDialog.Builder(this)
        alert.setTitle("food allergy")
        alert.setMessage("select type of food you want to delete")

        // Set an EditText view to get user input
        val spinner = Spinner(this)
        val spinnerAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,foodAllergy)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        alert.setView(spinner)


        alert.setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ ->
            try {
                val foodType = spinner.selectedItem.toString()
                foodAllergy.remove(foodType)
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, foodAllergy)
                cant_listView_id.adapter = adapter
            }catch (e:NumberFormatException){
                Toast.makeText(this, "Please input number", Toast.LENGTH_SHORT).show()
            }finally {
                return@OnClickListener
            }
        })

        alert.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { _, _ ->
                    return@OnClickListener
                })
        alert.show()
    }

    fun onSaveButtonClick(view:View){
        val map = HashMap<String,ArrayList<String>>()
        map["foodAllergy"] = foodAllergy
        map["name"] = arrayListOf(name_editText_id.text.toString())
        val filename = "whatTheEat"
        try  {
            val fos = this.openFileOutput(filename,android.content.Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(map)
            oos.close()
            Toast.makeText(applicationContext,"Data saved",Toast.LENGTH_LONG).show()
        }
        catch (e :FileNotFoundException) {
            e.printStackTrace()
        } catch(e:IOException){
             e.printStackTrace()
        }



    }



}
