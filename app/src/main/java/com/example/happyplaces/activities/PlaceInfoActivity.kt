package com.example.happyplaces.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.happyplaces.R
import com.example.happyplaces.database.PlaceApplication
import com.example.happyplaces.database.PlacesDao
import com.example.happyplaces.databinding.ActivityPlaceViewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlaceInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceViewBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // tool bar
        setSupportActionBar(binding?.tbShowPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Happy Place"
        binding?.tbShowPlace?.setNavigationOnClickListener{
            onBackPressed()
        }

        val placesDao = (application as PlaceApplication).db.placesDao()

        val id = intent.getIntExtra("id", 0)

        showData(id, placesDao)

    }

    private var binding: ActivityPlaceViewBinding? = null

    private fun showData(id: Int, placesDao: PlacesDao) {

        lifecycleScope.launch{

            placesDao.fetchPlaceById(id).collect { place ->

                if (place != null) {

                    binding?.ivDisplay?.setImageURI(place.image.toUri())
                    binding?.tvShowDescription?.text = place.description
                    binding?.tvShowTitle?.text = place.title
                    binding?.tvShowLocation?.text = place.location


                }

            }

        }

    }

}