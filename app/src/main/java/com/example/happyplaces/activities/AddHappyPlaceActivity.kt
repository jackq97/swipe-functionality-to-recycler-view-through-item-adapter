package com.example.happyplaces.activities

// TODO: null for binding

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.happyplaces.database.PlaceApplication
import com.example.happyplaces.database.PlaceEntity
import com.example.happyplaces.database.PlacesDao
import com.example.happyplaces.databinding.ActivityAddHappyPlaceBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*


class AddHappyPlaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        // onCreate

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        //date picker
        val dpd = DatePickerDialog(this, { _, Year, monthOfYear, dayOfMonth ->

            // Display Selected date in textbook
            // Adding 1 to our month cause months start from zero
            val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$Year"

            //displaying the data in our main tv
            binding?.etDate?.setText(selectedDate)

                                         }, year, month, day)

        //this prevents user from setting the date in future
        dpd.datePicker.maxDate = System.currentTimeMillis()

        // tool bar
        setSupportActionBar(binding?.tbHappyPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Happy Place"
        binding?.tbHappyPlace?.setNavigationOnClickListener{
            onBackPressed()
        }

        binding?.etDate?.setOnClickListener {

            dpd.show()
        }

        // uri var
        var fileUri: Uri? = null

        val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                    fileUri = data?.data!!
                    binding?.ivLocation?.setImageURI(fileUri)

                    // Log.e("image path", "$fileUri")

                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }

        binding?.tvAddImage?.setOnClickListener {

            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }

        }

        binding?.btnSave?.setOnClickListener {
            val snackBar = Snackbar.make(it, "Data Saved", Snackbar.LENGTH_SHORT)
            val db = ( application as PlaceApplication ).db.placesDao()
            if (fileUri != null) {
                addDataInDataBase(db, snackBar, fileUri!!)
            } else {
                Toast.makeText(this, "failed to fetch image", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

    }

    // main

    private var binding: ActivityAddHappyPlaceBinding? = null

    private fun addDataInDataBase(placesDao: PlacesDao,
                                  snackBar: Snackbar,
                                  image: Uri) {

        val title: String = binding?.etTitle?.text.toString()
        val description: String = binding?.etDescription?.text.toString()
        val date: String = binding?.etDate?.text.toString()
        val location: String = binding?.etLocation?.text.toString()
        //val longitude: String =
        //val latitude: String =

        if(title.isNotEmpty() && location.isNotEmpty()) {

            lifecycleScope.launch {

                placesDao.insert(PlaceEntity(title = title,
                    description = description,
                    date = date,
                    location = location,
                    image = image.toString()
                ))
                snackBar.show()
            }
        } else {
            snackBar.setTextColor(Color.RED)
            snackBar.setText("Text and Location can not be empty")
            snackBar.show()
        }

    }



}