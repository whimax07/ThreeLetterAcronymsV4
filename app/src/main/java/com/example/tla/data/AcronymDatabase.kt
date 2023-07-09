package com.example.tla.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Acronym::class], version = 1)
abstract class AcronymDatabase : RoomDatabase() {

    abstract fun acronymDao(): AcronymDao

    companion object {
        @Volatile
        private var Instance: AcronymDatabase? = null

        fun getDatabase(context: Context): AcronymDatabase {
            // If the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AcronymDatabase::class.java, "acronym_database")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}