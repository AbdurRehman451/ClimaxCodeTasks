package com.example.climaxtest.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PersonDao {

    @Query("Select * from personData order by id Desc ")
    fun getAllData() : LiveData<List<DataClass>>

    @Insert
    suspend fun insert(notes: DataClass)

    @Update
    suspend fun update(notes: DataClass)

    @Delete
    suspend fun delete(notes: DataClass)


}