package com.example.climaxtest.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.climaxtest.Converters
import java.io.Serializable

@Entity( tableName = "personData")
data class DataClass (
    @PrimaryKey(autoGenerate = true)
    var id :Int,
    val name: String,
    val age: Int,
    val imageUrl: String,
    @TypeConverters(Converters::class)
    val hobbies: List<String>
    ): Serializable