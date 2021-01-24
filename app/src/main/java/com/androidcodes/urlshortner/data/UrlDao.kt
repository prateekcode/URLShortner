package com.androidcodes.urlshortner.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androidcodes.urlshortner.data.model.UrlData


@Dao
interface UrlDao {

    @Query("SELECT * FROM url_table")
    fun getAllData(): LiveData<List<UrlData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(urlData: UrlData)

    @Query("DELETE FROM url_table")
    suspend fun deleteAll()

}