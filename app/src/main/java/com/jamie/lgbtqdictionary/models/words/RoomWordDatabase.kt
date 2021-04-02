package com.jamie.lgbtqdictionary.models.words

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [RoomWord::class], version = 1)
abstract class RoomWordDatabase : RoomDatabase() {
    abstract fun noteDao(): RoomWordDao

    companion object {
        @Volatile private var instance: RoomWordDatabase? = null

        @Synchronized
        fun getInstance(context: Context): RoomWordDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomWordDatabase::class.java, "word_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }

}

// source code: https://codinginflow.com/tutorials/android/room-viewmodel-livedata-recyclerview-mvvm/part-3-dao-roomdatabase
