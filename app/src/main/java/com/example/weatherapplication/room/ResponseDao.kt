package com.example.weatherapplication.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapplication.model.Response

@Dao
interface ResponseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addResponse(response: Response)

    @Query("SELECT * FROM response_table")
    fun readAllData(): LiveData<List<Response>>

    @Query("DELETE FROM response_table")
    suspend fun deleteAllData()
}