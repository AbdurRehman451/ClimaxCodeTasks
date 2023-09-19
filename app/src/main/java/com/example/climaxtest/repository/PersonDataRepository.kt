package com.example.climaxtest.repository

import androidx.lifecycle.LiveData
import com.example.climaxtest.room.DataClass
import com.example.climaxtest.room.PersonDao

class PersonDataRepository(private val personData: PersonDao) {

    fun getDao(): LiveData<List<DataClass>> {
        return personData.getAllData()
    }

    suspend fun insert(data: DataClass) {
        return personData.insert(data)
    }

    suspend fun update(data: DataClass){
        return personData.update(data)
    }

    suspend fun delete(data: DataClass){
        return personData.delete(data)
    }

}