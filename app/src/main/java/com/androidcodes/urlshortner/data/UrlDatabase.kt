package com.androidcodes.urlshortner.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.androidcodes.urlshortner.data.model.UrlData


@Database(entities = [UrlData::class], version = 3, exportSchema = true)
abstract class UrlDatabase : RoomDatabase() {

    abstract fun urlDao(): UrlDao

    companion object {
        @Volatile
        private var INSTANCE: UrlDatabase? = null

        fun getDatabase(context: Context): UrlDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                UrlDatabase::class.java, "url_database"
            )
                .fallbackToDestructiveMigration()
                .build()
    }

}