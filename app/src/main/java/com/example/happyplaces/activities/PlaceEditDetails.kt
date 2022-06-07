package com.example.happyplaces.activities


import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.happyplaces.database.PlaceApplication
import com.example.happyplaces.database.PlaceEntity
import com.example.happyplaces.database.PlacesDao
import com.example.happyplaces.databinding.ActivityPlaceviewDetailsBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

// TODO: add comments

class PlaceEditDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceviewDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // onCreate

        val placesDao = (application as PlaceApplication).db.placesDao()
        val id = intent.getIntExtra("id", 0)

        setDetails(id, placesDao)


        binding?.etDate?.setOnClickListener { datePickerDialog() }

        binding?.tvAddImage?.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        binding?.btnUpdate?.setOnClickListener {
            val snackBar = Snackbar.make(it, "Data Saved", Snackbar.LENGTH_SHORT)
            updateItemDb(id, placesDao, snackBar)
        }
    }

    // main
    private var binding: ActivityPlaceviewDetailsBinding? = null

    private var selectedImagePath: String?  = null

    private fun setDetails(id: Int, placesDao: PlacesDao) {

        lifecycleScope.launch {
            placesDao.fetchPlaceById(id).collect {

                binding?.etTitle?.setText(it.title)
                binding?.etDescription?.setText(it.description)
                binding?.etDate?.setText(it.date)
                binding?.etLocation?.setText(it.location)
                binding?.ivLocation?.setImageURI(it.image.toUri())
                selectedImagePath = it.image

            }
        }
    }

    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    fun datePickerDialog() {

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
        dpd.show()
    }

    var fileUri: Uri? = null

    private val startForProfileImageResult =
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

    private fun updateItemDb(id: Int,
                             placesDao: PlacesDao,
                             snackBar: Snackbar
    ) {

        val title: String = binding?.etTitle?.text.toString()
        val description: String = binding?.etDescription?.text.toString()
        val date: String = binding?.etDate?.text.toString()
        val location: String = binding?.etLocation?.text.toString()
        var imagePath: String? = null
        //val longitude: String =
        //val latitude: String =

        if (fileUri == null) {
            imagePath = selectedImagePath
        } else {
            imagePath = fileUri.toString()
        }

        lifecycleScope.launch {

            placesDao.update(PlaceEntity(
                id,
                title,
                description,
                imagePath.toString(),
                date,
                location,
            ))

            snackBar.show()
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}