package com.example.aplikasidicodingeventnavigationdanapi.ui.upcoming

import android.app.Application
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

class UpcomingViewModel(application: Application) : AndroidViewModel(application) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listEventsItemUpcoming = MutableLiveData<List<EventData>?>()
    val listEventsItemUpcoming: LiveData<List<EventData>?> = _listEventsItemUpcoming

    private val _stored = MutableLiveData<List<EventData>?>()
    val stored: LiveData<List<EventData>?> = _stored

    init {
        upcomingEvents()
    }

    private fun upcomingEvents() {
        _isLoading.value = true
        viewModelScope.launch {
            delay(4000)
            try {
                val response = EventAPIService.create().getActiveEvents()
                response.enqueue(object : Callback<Event> {
                    override fun onResponse(call: Call<Event>, response: Response<Event>) {
                        _isLoading.value = false
                        if (response.isSuccessful) {
                            response.body()?.let {
                                _listEventsItemUpcoming.value = it.listEvents

                                if (_stored.value == null) {
                                    _stored.value = it.listEvents
                                }
                            }
                        } else {
                            Toast.makeText(getApplication(), "Failed to Fetch API", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Event>, t: Throwable) {
                        Toast.makeText(getApplication(), "Failed to load event", Toast.LENGTH_SHORT).show()
                    }
                })
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchEvents(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = EventAPIService.create().searchEvents(query)
                response.enqueue(object : Callback<Event> {
                    override fun onResponse(call: Call<Event>, response: Response<Event>) {
                        _isLoading.value = false
                        if (response.isSuccessful) {
                            _listEventsItemUpcoming.value = response.body()?.listEvents
                        } else {
                            Toast.makeText(getApplication(), "Failed to Fetch API", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Event>, t: Throwable) {
                        _isLoading.value = false
                        Toast.makeText(getApplication(), "Failed to Fetch API", Toast.LENGTH_SHORT).show()
                    }
                })
            } finally {
                _isLoading.value = false
            }
        }
    }
}