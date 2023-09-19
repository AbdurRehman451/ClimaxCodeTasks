package com.example.climaxtest.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.climaxtest.R
import com.example.climaxtest.databinding.SelectImage
import kotlinx.android.synthetic.main.activity_select_image.*
import kotlinx.android.synthetic.main.fragment_image_display.*
import java.io.File
import java.io.IOException

class SelectImageActivity : AppCompatActivity() {
    lateinit var binding: SelectImage
    var PICK_IMAGE = 1
    private lateinit var imageDisplayFragment: ImageDisplayFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_select_image)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_select_image)
        setContentView(binding.root)
        imageDisplayFragment = ImageDisplayFragment()

        // Add the initial fragment to the container (if needed)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, imageDisplayFragment)
            .commit()
        select_image.setOnClickListener {
            onSelectSource()
        }
    }

    fun onSelectSource() {
        try {
            val dialogView = View.inflate(this, R.layout.dialogbox_select_images, null)
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val ivCancel = dialogView.findViewById<ImageView>(R.id.cancelBtn)
            val ivGallery = dialogView.findViewById<ImageView>(R.id.iv_gallery)
            val ivCapture = dialogView.findViewById<ImageView>(R.id.iv_capture)
            ivCancel.setOnClickListener { view: View? -> alertDialog.dismiss() }
            ivGallery.setOnClickListener { view: View? ->
                alertDialog.dismiss()
                addImages()
            }
            ivCapture.setOnClickListener { view: View? ->
                alertDialog.dismiss()
               onCaptureProductImage()
            }
            val window = alertDialog.window
            val wlp = window!!.attributes
            wlp.gravity = Gravity.CENTER
            wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
            window.attributes = wlp
            alertDialog.setView(dialogView)
            alertDialog.show()
            alertDialog.setCancelable(false)
        } catch (e: Exception) {
            Log.i("EXC", "showInfoBanner: exception $e")
        }
    }

    fun addImages() {
        // ((CreateProductsActivity) context).onUploadProductImage();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onUploadSingleImageForHigher()
        } else {
            onUploadImage()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    fun onUploadSingleImageForHigher() {
        val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
        // starting activity on below line.
        startActivityForResult(intent, 122)
    }

    fun onUploadImage() {
        val PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (!hasPermissions(this, *PERMISSIONS)) {
            requestPermissions(PERMISSIONS, PICK_IMAGE)
        } else {
            dispatchGalleryIntent()
        }
    }


    fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    fun onCaptureProductImage() {
        val PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )

        if (!hasPermissions(this, *PERMISSIONS)) {
            requestPermissions(PERMISSIONS, 123)
        } else {
            dispatchCameraIntent()
        }
    }

   /* @SuppressLint("IntentReset")
    fun dispatchCameraIntent(captureCode: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (applicationContext.packageManager.hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY
            )
        ) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()

                // Error occurred while creating the File
            }
            Log.i(
                com.stocksmaze.seller.activities.products.CreateProductsActivity.TAG,
                "dispatchTakePictureIntent: Photo File $photoFile"
            )
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile
                )
                mPhotoFile = photoFile
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, captureCode)
            }
        }
    }*/

    @SuppressLint("IntentReset")
    fun dispatchGalleryIntent() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickPhoto.type = "image/*"
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(pickPhoto, PICK_IMAGE)
        /* Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.setType("image/ *");
        imagePickerLauncher.launch(pickPhoto);*/
    }

    private fun dispatchCameraIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(takePictureIntent, 124)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE -> {
                    // Handle the result for the gallery intent
                    if (data != null) {
                        val selectedImageUri = data.data
                        val imageBitmap = selectedImageUri?.let { loadBitmapFromUri(it) }
                        imageDisplayFragment.setImage(imageBitmap)
                        showImageDisplayFragment()
                        // Now, you can use the selectedImageUri to do something with the selected image.
                        // For example, display it in an ImageView or process it in some way.
                    }
                }
                124 -> {
                    // Handle the result for the camera intent
                    // If you have captured an image, you can access it here.
                    if (data != null && data.extras != null) {
                        val imageBitmap = data.extras!!.get("data") as Bitmap
                        val selectedImageUri = data.data
                        val imageBitmap2 = selectedImageUri?.let { loadBitmapFromUri(it) }
                        imageDisplayFragment.setImage(imageBitmap)
                        showImageDisplayFragment()
                        // Now, you can use the imageBitmap to do something with the captured image.
                        // For example, display it in an ImageView or save it to storage.
                    }
                }
                122 -> {
                    if (data != null) {
                        val imageUris = mutableListOf<Uri>()

                        // Check for multiple selected images
                        if (data.clipData != null) {
                            val clipData = data.clipData
                            if (clipData != null) {
                                for (i in 0 until clipData.itemCount) {
                                    val uri = clipData.getItemAt(i).uri
                                    imageUris.add(uri)
                                    val selectedImageUri = data.data
                                    val imageBitmap = selectedImageUri?.let { loadBitmapFromUri(it) }
                                    imageDisplayFragment.setImage(imageBitmap)
                                }
                            }
                        } else if (data.data != null) {
                            // Single image selected
                            val uri = data.data
                            if (uri != null) {
                                imageUris.add(uri)
                                val selectedImageUri = data.data
                                val imageBitmap = selectedImageUri?.let { loadBitmapFromUri(it) }
                                imageDisplayFragment.setImage(imageBitmap)
                            }
                        }

                        // Handle the list of selected image URIs here
                        // You can pass this list to your fragment or perform any desired actions.
                    }
                }

                // Handle other request codes if needed
            }
        }
    }
    private fun loadBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private fun showImageDisplayFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, imageDisplayFragment)
        transaction.addToBackStack(null) // Optional: Add to back stack for fragment navigation
        transaction.commit()
    }

}

class ImageDisplayFragment : Fragment() {

    // Define any necessary variables or methods to display the selected images

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout here (e.g., a RecyclerView for displaying images)
        val view = inflater.inflate(R.layout.fragment_image_display, container, false)

        // Initialize and populate the UI elements to display selected images here

        return view
    }
    fun setImage(imageBitmap: Bitmap?) {
        imageView?.setImageBitmap(imageBitmap)
    }
}