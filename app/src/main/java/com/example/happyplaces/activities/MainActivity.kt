package com.example.happyplaces.activities

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.database.PlaceApplication
import com.example.happyplaces.database.PlaceEntity
import com.example.happyplaces.database.PlacesDao
import com.example.happyplaces.databinding.ActivityMainBinding
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// TODO: make recycle view gone after removing all activities

class MainActivity : AppCompatActivity(), Adapter.OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        // onCreate

        swipeGesture()

        askPermission()

        // val to access db
        val placesDao = (application as PlaceApplication).db.placesDao()

        lifecycleScope.launch {

            placesDao.fetchAllPlaces().collect {
                val list = ArrayList(it)
                settingUpRecyclerView(list)
            }



        }

        binding?.fabHappyPlace?.setOnClickListener {
        // moving to our add happy place intent
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onItemClick(position: Int, id: Int) {
        val intent = Intent(this, PlaceViewActivity::class.java)
        intent.putExtra("id", id)
        Log.i("room database: id", "$id")
        startActivity(intent)

    }
    // main

    private var binding: ActivityMainBinding? = null



    private fun askPermission() {

        PermissionX.init(this@MainActivity)
            .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "App won't work with out the location permission", "OK", "Cancel")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "You need to allow necessary permissions in Settings manually", "OK", "Cancel")
            }
            .request { allGranted, _, _ ->
                if (allGranted) {

                } else {
                   finish()
                }
            }
    }

    var itemAdapter: Adapter? = null

    private fun settingUpRecyclerView(placesList: ArrayList<PlaceEntity>){

        if (placesList.isNotEmpty()) {

            itemAdapter = Adapter(placesList, this)

            binding?.rvMain?.layoutManager = LinearLayoutManager(this)
            binding?.rvMain?.adapter = itemAdapter

        }
    }

    private fun swipeGesture() {

        val placesDao = (application as PlaceApplication).db.placesDao()

        val swipeGesture = object : SwipeGesture(this) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when (direction) {

                    ItemTouchHelper.LEFT -> {
                        Toast.makeText(applicationContext, "swiped left", Toast.LENGTH_SHORT).show()
                    }

                    ItemTouchHelper.RIGHT -> {
                        // getting id from our adapter function
                        val id = itemAdapter?.getId(viewHolder.bindingAdapterPosition)
                        // passing the id to delete function
                        if (id != null) {
                            deleteEntryFromDb(placesDao, id)
                        } else {
                            Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding?.rvMain)

    }

    fun deleteEntryFromDb (placesDao: PlacesDao, id: Int) {

        lifecycleScope.launch{

            placesDao.delete(PlaceEntity(id))

        }

    }

}