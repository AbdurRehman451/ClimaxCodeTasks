package com.example.climaxtest.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.climaxtest.R
import com.example.climaxtest.activities.toast
import com.example.climaxtest.repository.PersonDataRepository
import com.example.climaxtest.room.DataClass
import com.example.climaxtest.room.RoomDataBase
import com.example.climaxtest.viewmodels.MainViewModel
import com.example.climaxtest.viewmodels.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_data.*
import kotlinx.android.synthetic.main.item_hobby.*
import kotlinx.android.synthetic.main.recyler_list.*
import kotlinx.coroutines.launch


class AddDataFragment : BaseFragment() {
    val REQUEST_IMAGE_PICK = 1
    val REQUEST_IMAGE_CAPTURE = 2

    lateinit var mainViewModel: MainViewModel
    private var datas: DataClass? = null
    private val TAG = "AddDataFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val quotesDao = RoomDataBase.getDataBase(requireContext()).personDao()
        val repository = PersonDataRepository(quotesDao)

        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_data, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            datas = AddDataFragmentArgs.fromBundle(it).data
            nameTextView.setText(datas?.name)
            ageTextView.setText(datas?.age.toString())
            editTextImageUrl.setText(datas?.imageUrl.toString())
            val joinedString = datas?.hobbies?.joinToString(", ")
            editTextHobbies.setText(joinedString)


        }
        saveButton.setOnClickListener {
            val name = nameTextView.text.toString().trim()
            val age = ageTextView.text.toString().trim()
            val hobbies = editTextHobbies.text.toString().split(",").map { it.trim() }
            val imgeurl = editTextImageUrl.text.toString().trim()

            if (name.isEmpty()) {
                nameTextView.error = "name required"
                nameTextView.requestFocus()
                return@setOnClickListener
            }
            if (age.isEmpty()) {
                ageTextView.error = "age required"
                ageTextView.requestFocus()
                return@setOnClickListener
            }
            if (hobbies.isEmpty()) {
                ageTextView.error = "hobbies required"
                ageTextView.requestFocus()
                return@setOnClickListener
            }
            /*if (imgeurl.isEmpty()) {
                ageTextView.error = "Note required"
                ageTextView.requestFocus()
                return@setOnClickListener
            }*/
            launch {
                val data = DataClass(0, name, age.toInt(), imgeurl, hobbies)
                context?.let {
//                    NotesDatabase.getDataBase(it).notesDao().insertNotes(note)
                    if (datas == null) {
                        mainViewModel.insert(data)
                        it.toast("Data Saved")
                    } else {
                        data.id = datas!!.id
                        mainViewModel.update(data)
                        it.toast("Data updated")

                    }

                    val action = AddDataFragmentDirections.actionAddDataFragmentToHomeFragment()
                    findNavController().navigate(action)

                }
            }
        }
        editTextImageUrl.setOnClickListener {
           openImagePicker()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteItem -> if (datas != null) deleteNote() else context?.toast("canNot Delete")
        }
        return super.onOptionsItemSelected(item)

    }

    private fun deleteNote() {
        AlertDialog.Builder(context).apply {
            setTitle("Are you sure?")
            setMessage("can not undo this")
            setPositiveButton("Yes") { _, _ ->
                launch {
                    mainViewModel.delete(datas!!)
                    val action = AddDataFragmentDirections.actionAddDataFragmentToHomeFragment()
                    findNavController().navigate(action)
                }
            }
            setNegativeButton("No") { _, _ ->

            }
        }.create().show()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.option_menu,menu)
    }





    private fun openImagePicker() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_PICK -> {
                    val imageUri = data?.data
                    if (imageUri != null) {
                        // Handle the selected image URI
                        val imageUrl = imageUri.toString()
                        editTextImageUrl.setText(imageUrl)
                    }
                }
                REQUEST_IMAGE_CAPTURE -> {
                    // Handle the captured photo (if needed)
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    if (imageBitmap != null) {
                        // Convert the Bitmap to a URL or save it to storage
                        // and then set the URL in the EditText
                    }
                }
            }
        }
    }

}