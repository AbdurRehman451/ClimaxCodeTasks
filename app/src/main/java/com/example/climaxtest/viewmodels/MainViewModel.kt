package com.example.climaxtest.viewmodels

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climaxtest.R
import com.example.climaxtest.repository.PersonDataRepository
import com.example.climaxtest.room.DataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: PersonDataRepository) : ViewModel() {

    fun getDao(): LiveData<List<DataClass>> {

        return repository.getDao()
    }

    fun insert(data: DataClass) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(data)
        }

    }
    fun update(data: DataClass){
        viewModelScope.launch(Dispatchers.IO) {repository.update(data)  }
    }

    fun delete(data: DataClass){
        viewModelScope.launch(Dispatchers.IO) {repository.delete(data)  }
    }


}