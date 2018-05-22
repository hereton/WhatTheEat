package com.example.ton.whattheeat

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.util.Base64
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import android.graphics.BitmapFactory




class ProfileActivity : AppCompatActivity() {

    private lateinit var foodAllergy : ArrayList<String>
    private lateinit var typeOfFoodList:ArrayList<String>
    private lateinit var map: HashMap<String,ArrayList<String>>
    private lateinit var imgProfileString:ArrayList<String>
    private var requestCode = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        if(supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        typeOfFoodList = arrayListOf("meat","seafood","soup")
        foodAllergy = ArrayList()
        imgProfileString = ArrayList()
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
            println(map["imageProfile"].toString())
            val stringBitmap  = map["imageProfile"]!![0]
            val bitmap = stringToBitMap(stringBitmap)
            profile_image_id.setImageBitmap(bitmap)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == requestCode && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            val uri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
                imgProfileString.add(bitMapToString(bitmap))
                profile_image_id.setImageBitmap(bitmap)
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    private fun bitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
   private fun stringToBitMap(encodedString: String): Bitmap? {
        return try {
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }

    }
    fun onImageHandleClick(view:View){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Select image profile"),requestCode)
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
                    if(foodAllergy.size == typeOfFoodList.size){
                        foodAllergy.remove(foodType)
                        Toast.makeText(this,"You have to eat something",Toast.LENGTH_SHORT).show()
                    }
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
        map.clear()
        map["foodAllergy"] = foodAllergy
        map["name"] = arrayListOf(name_editText_id.text.toString())
        map["imageProfile"] = imgProfileString
        val filename = "whatTheEat"
        try  {
            val fos = openFileOutput(filename,android.content.Context.MODE_PRIVATE)
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
