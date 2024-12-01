package com.xpi.kollect.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.xpi.kollect.R
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_image_collect.*
import kotlinx.android.synthetic.main.fragment_video_collect.*
import java.text.DateFormat
import java.util.*

class ImageCollectFragment : Fragment(R.layout.fragment_image_collect) {

    private lateinit var database: DatabaseReference

    private val storageRef = Firebase.storage.reference
    private var imageUri : Uri? = null
    private var currentLocation : Location? = null
    private var downloadedImageUri : String? = null
    private val cameraRequestId = 1222
    private val RESULT_LOAD_IMAGE = 123
    val IMAGE_CAPTURE_CODE = 654

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // creating a database reference

        database = Firebase.database.reference

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        gallery.setOnClickListener {
            //ImagePicker.with(this).galleryOnly().galleryMimeTypes(arrayOf("image/*")).crop()
                //.maxResultSize(400, 400).start()

            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, RESULT_LOAD_IMAGE)
        }
        camera.setOnClickListener{
            //ImagePicker.with(this).cameraOnly().crop().start()

            //val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            //startActivityForResult(intent, cameraRequestId)

            openCamera()
        }





        // Saving image to Firebase Storage

        saveImage.setOnClickListener {

            if(imageUri != null)
            {
                getCurrentLocation()
                if(currentLocation != null) {

                    if(hasInternetConnection()){
                        saveCollectedData()
                    }
                    else{
                        val message = "Vous n'avez pas de connexion internet. Nous allons enregistrer vos données en local"
                        errorMessage(message)
                    }

                }
                else{
                    val message = "Veillez activer la localisation"
                    errorMessage(message)
                    getCurrentLocation()
                }
            }
            else {
                val message = "Veillez selectionner une image"
                errorMessage(message)
            }

        }
    }


    //TODO opens camera so that user can capture image
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, cameraRequestId)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            if(requestCode == cameraRequestId){
                //val image:Bitmap = data?.extras?.get("data") as Bitmap
                //Log.i(TAG, "onActivityResult: ")
                Log.e(TAG, "Example Item: " + data?.data)
                //imageUri  = data?.getStringExtra(MediaStore.EXTRA_OUTPUT) as Uri
                picker_image.setImageURI(imageUri)
                //picker_image.setImageBitmap(image)
                if(imageUri != null) {
                    successMessage("image loaded from Camera")
                }
                // extract the file name with extension
            }
             else if(requestCode == RESULT_LOAD_IMAGE)
             {
                imageUri = data?.data
                picker_image.setImageURI(data?.data)
                successMessage("Image loaded from Gallery")
            }

        } // End

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }

    val sd = getFileName(requireContext().applicationContext, imageUri!!)
    val uploadTask = storageRef.child("photos/$sd").putFile(imageUri!!)


    private fun currentDateTime(): String? {

        val calendar = Calendar.getInstance().time
        return DateFormat.getDateTimeInstance().format(calendar)

    }

    private fun saveCollectedData()
    {
        val sd = getFileName(requireContext().applicationContext, imageUri!!)

        val creationDate = currentDateTime()

        Toast.makeText(requireContext().applicationContext, creationDate,Toast.LENGTH_LONG).show()

        // Upload Task with upload to directory 'file'
        // and name of the file remains same
        val uploadTask = storageRef.child("photos/$sd").putFile(imageUri!!)

        // On success, download the file URL and display it
        uploadTask.addOnSuccessListener {
            // using glide library to display the image
            storageRef.child("photos/$sd").downloadUrl.addOnSuccessListener {
                downloadedImageUri = it.toString()
                Glide.with(this)
                    .load(it)
                    .into(picker_image)

                downloadedImageUri?.let { it1 -> Log.e("Firebase", it1) }
                saveData()
            }.addOnFailureListener {
                Log.e("Firebase", "Failed in downloading")
            }
        }.addOnFailureListener {
            Log.e("Firebase", "Image Upload fail")
        }
    }

    private fun saveData(){
        val commentaire = commentEditText.text.toString()
        val createdBy = "Yvan Takoumbo"
        val creationDateTime = currentDateTime()
        val latitude = currentLocation?.latitude
        val longitude = currentLocation?.longitude
        val collectedData = downloadedImageUri
        val collectId = database.push().key!!

        val myCollection = com.xpi.kollect.model.Collection(collectId, collectedData, commentaire,creationDateTime, longitude.toString(), latitude.toString(), createdBy)

        database.child("collections").child("images").child(collectId).setValue(myCollection).addOnCompleteListener {

            val message = "Vos données ont été enregistrés avec succès!"
            successMessage(message)
            commentEditText.text.clear()

        }.addOnFailureListener{err ->
            errorMessage(err.message)
        }

    }

    private fun successMessage(message: String?)
    {
        val toast = Toast.makeText(requireContext().applicationContext, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.START, 200, 200)
        toast.show()
    }
    private fun errorMessage(message : String?)
    {
        val toast = Toast.makeText(requireContext().applicationContext, "Une erreur c'est produite : $message", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.START, 200, 200)
        toast.show()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getCurrentLocation()
    {
        // Check if permissions were granted
        if (checkPermissions()) {
            if (isLocationEnabled()){

                if (ActivityCompat.checkSelfPermission(
                        requireContext().applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext().applicationContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location : Location? = task.result
                        if (location == null)
                        {
                            val message = "Impossible d'accèder à votre localisation"
                            errorMessage(message)
                        }
                        else{
                            val message = "Position actuelle enregistrer avec succès"
                            successMessage(message)
                            currentLocation = location
                        }
                    }
            }
            else{
                // Opening settings for user to enable location

                Toast.makeText(requireContext().applicationContext, "Veillez activer la localisation", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else{
            // Resquesting permissions
            requestPermission()
        }


    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION=100
    }

    private fun checkPermissions() : Boolean{
        return ActivityCompat.checkSelfPermission(
            requireContext().applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext().applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(requireContext().applicationContext, "Autorisations accepter", Toast.LENGTH_LONG).show()
                getCurrentLocation()
            }
            else
            {
                Toast.makeText(requireContext().applicationContext, "Autorisation réfuser. L'application ne pourra pas fonctionner", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isLocationEnabled() : Boolean
    {
        val locationManager : LocationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun hasInternetConnection() : Boolean{

        val connectivityManager = activity?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false

            }
        } else {

            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false

                }
            }
        }
        return false
    }

    private fun askMediaPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(requireContext().applicationContext,Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(requireContext().applicationContext,Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permission, 112)
            }
        }

    }

    private fun askPermission(){
        val readImagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
        if(ContextCompat.checkSelfPermission(requireContext().applicationContext, readImagePermission) == PackageManager.PERMISSION_GRANTED){
            //permission granted
        } else {
            //permission not granted
        }
    }

}
