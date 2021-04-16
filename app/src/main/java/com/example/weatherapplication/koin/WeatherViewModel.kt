package com.example.weatherapplication.koin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.api.RetrofitInstance
import com.example.weatherapplication.model.Response
import com.example.weatherapplication.room.ResponseDatabase
import com.example.weatherapplication.room.ResponseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    val weather = MutableLiveData<Response>()
    private val repository: ResponseRepository
    lateinit var allData: LiveData<List<Response>>

    init {
        Log.d("viewmodel", "init")
        val responseDao = ResponseDatabase.getDatabase(application).responseDao()
        repository = ResponseRepository(responseDao)
        viewModelScope.launch(Dispatchers.IO) { allData = repository.readAllData() }
        getResult()
    }

    fun getResult() {
        Log.d("viewmodel", "get result")
        RetrofitInstance.weatherApi.getWeather(53.893009F, 27.567444F, "metric").enqueue(object :
            Callback<Response> {
            // Network Errors here
            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.d("viewmodel", "on failure")
                Log.d("viewmodel", t.message.toString())

                if (allData.value != null) {
                    weather.value = allData.value!![0]
                }
            }

            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    val responseBody: Response = response.body()!!
                    Log.d("viewmodel", responseBody.toString())

//                    weather.value = responseBody
                    Log.d("viewmodel", "_____________________________________________")
                    Log.d("viewmodel", "Before deletion")
                    Log.d("viewmodel", allData.value?.get(0).toString())
                    Log.d("viewmodel", "After deletion")
                    deleteAllData()
                    Log.d("viewmodel", allData.value?.get(0).toString())

                    Log.d("viewmodel", "Before addibg")
                    Log.d("viewmodel", allData.value?.get(0).toString())
                    Log.d("viewmodel", "After adding")
                    responseBody?.let { addResponse(it) }
                    Log.d("viewmodel", allData.value?.get(0).toString())

                    weather.value = allData.value?.get(0)
                } else {
                    Log.d("viewmodel", "on response, but not successful")
                    Log.d("viewmodel", response.errorBody().toString())

                    if (allData.value?.isNotEmpty()!!) {
                        weather.value = allData.value!![0]
                    }
                }
            }
        })
    }

    fun addResponse(response: Response) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.addResponse(response)
            allData = repository.readAllData()
        }

    fun deleteAllData() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllData()
            allData = repository.readAllData()
        }
}