package com.theblackthorn.travelbook

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


var namesArray = ArrayList<String>()
var locationArray = ArrayList<LatLng>()


class MainActivity : AppCompatActivity() {



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_place,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item!!.itemId == R.id.add_place) {

            val intent = Intent(applicationContext, MapsActivity::class.java)
            intent.putExtra("info", "new")
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {


        try {

            val database = openOrCreateDatabase("Places", Context.MODE_PRIVATE, null)
            val cursor = database.rawQuery("SELECT * FROM places", null)

            val nameIndex = cursor.getColumnIndex("name")
            val latitudeIndex = cursor.getColumnIndex("latitude")
            val longitudeIndex = cursor.getColumnIndex("longitude")

            cursor.moveToFirst()

            namesArray.clear()
            locationArray.clear()

            while (cursor != null) {

                val nameFromDatabase = cursor.getString(nameIndex)
                val latitudeFromDatabase = cursor.getString(latitudeIndex)
                val longitudeFromDatabase = cursor.getString(longitudeIndex)


                namesArray.add(nameFromDatabase)

                val latitudeCoordinate = latitudeFromDatabase.toDouble()
                val longitudeCoordinate = longitudeFromDatabase.toDouble()

                val location = LatLng(latitudeCoordinate, longitudeCoordinate)

                locationArray.add(location)

                cursor.moveToNext()

            }


        } catch (e:Exception){
            e.printStackTrace()
        }

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, namesArray)
        listView.adapter = arrayAdapter

        listView.setOnItemClickListener{ parent: AdapterView<*>?, view: View?, position: Int, id: Long ->

            val intent = Intent(applicationContext,MapsActivity::class.java)
            intent.putExtra("info","old")
            intent.putExtra("name", namesArray[position])
            intent.putExtra("latitude", locationArray[position].latitude)
            intent.putExtra("longitude", locationArray[position].longitude)
            startActivity(intent)

        }

        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }


}
