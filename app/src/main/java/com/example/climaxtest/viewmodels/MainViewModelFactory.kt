package com.example.climaxtest.viewmodels

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.climaxtest.repository.PersonDataRepository

class MainViewModelFactory(private val repository: PersonDataRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}