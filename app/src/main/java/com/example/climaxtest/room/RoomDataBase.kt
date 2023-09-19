package com.example.climaxtest.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.climaxtest.Converters

@Database(entities = [DataClass ::class] , version = 1)
@TypeConverters(Converters::class)
abstract class RoomDataBase : RoomDatabase() {

    abstract fun personDao(): PersonDao

    companion object{
        private var INSTANCE : RoomDataBase? =null

        fun getDataBase(context: Context) :RoomDataBase{

            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context,RoomDataBase::class.java,"notesDatabase")
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}