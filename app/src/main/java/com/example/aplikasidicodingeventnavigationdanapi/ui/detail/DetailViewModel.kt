package com.example.aplikasidicodingeventnavigationdanapi.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aplikasidicodingeventnavigationdanapi.dataAPI.Event
import com.example.aplikasidicodingeventnavigationdanapi.dataAPI.EventAPIService
import com.example.aplikasidicodingeventnavigationdanapi.dataAPI.EventData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _eventDetail = MutableLiveData<EventData?>()
    val eventDetail: LiveData<EventData?> = _eventDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getEventDetail(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            delay(4000)
            try {
                val response = EventAPIService.create().getEventDetail(id)
                response.enqueue(object : Callback<Event> {
                    override fun onResponse(call: Call<Event>, response: Response<Event>) {
                        _isLoading.value = false
                        if (response.isSuccessful) {
                            response.body()?.let {
                                _eventDetail.value = it.event
                            }
                        } else {
                            _errorMessage.value = "Failed to Fetch API"
                        }
                    }

                    override fun onFailure(call: Call<Event>, t: Throwable) {
                        _isLoading.value = false
                        _errorMessage.value = "Failed to Fetch API"
                    }
                })
            }catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Failed to load events: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}