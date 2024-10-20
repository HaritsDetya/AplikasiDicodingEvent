package com.example.aplikasidicodingeventnavigationdanapi.ui.home

import android.app.Application
import android.util.Log
import android.widget.Toast
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

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    companion object{
        private const val TAG = "HomeViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listEventsItemUpcoming = MutableLiveData<List<EventData>?>()
    val listEventsItemUpcoming: LiveData<List<EventData>?> = _listEventsItemUpcoming

    private val _listEventsItemFinished = MutableLiveData<List<EventData>?>()
    val listEventsItemFinished: LiveData<List<EventData>?> = _listEventsItemFinished

    init {
        upcomingEvent()
        finishedEvents()
    }

    private fun upcomingEvent() {
        _isLoading.value = true
        viewModelScope.launch {
            delay(4000)
            try {
                val result = EventAPIService.create().getActiveEvents()
                result.enqueue(object : Callback<Event> {
                    override fun onResponse(call: Call<Event>, response: Response<Event>) {
                        _isLoading.value = false
                        if (response.isSuccessful) {
                            _listEventsItemUpcoming.value = response.body()?.listEvents
                        } else {
                            Log.e(TAG, "upcomingEvent: Failure - ${response.message()}")
                            Toast.makeText(getApplication(), "Failed to Fetch API", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Event>, t: Throwable) {
                        Toast.makeText(getApplication(), "Failed to load events: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun finishedEvents() {
        _isLoading.value = true
        viewModelScope.launch {
            delay(4000)
            try {
                val result = EventAPIService.create().getFinishedEvents()
                result.enqueue(object : Callback<Event> {
                    override fun onResponse(call: Call<Event>, response: Response<Event>) {
                        _isLoading.value = false
                        if (response.isSuccessful) {
                            val event = response.body()?.listEvents
                            _listEventsItemFinished.value = event?.take(5)
                        } else {
                            Toast.makeText(getApplication(), "Failed to Fetch API", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Event>, t: Throwable) {
                        Toast.makeText(getApplication(), "Failed to load events: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } finally {
                _isLoading.value = false
            }
        }
    }
}