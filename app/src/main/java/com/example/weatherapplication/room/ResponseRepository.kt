package com.example.weatherapplication.room

import com.example.weatherapplication.model.Response

class ResponseRepository(private val responseDao: ResponseDao) {

    suspend fun readAllData() = responseDao.readAllData()

    suspend fun addResponse(response: Response) = responseDao.addResponse(response)

    suspend fun deleteResponse(response: Response) = responseDao.deleteResponse(response)
}