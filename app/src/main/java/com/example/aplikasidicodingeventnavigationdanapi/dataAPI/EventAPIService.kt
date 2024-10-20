package com.example.aplikasidicodingeventnavigationdanapi.dataAPI

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventAPIService {
    @GET("events?active=1")
    fun getActiveEvents(): Call<Event>

    @GET("events?active=0")
    fun getFinishedEvents(): Call<Event>

    @GET("events?active=-1")
    fun searchEvents(@Query("q") query: String): Call<Event>

    @GET("events/{id}")
    fun getEventDetail(@Path("id") id: String): Call<Event>

    companion object {
        private const val BASE_URL = "https://event-api.dicoding.dev/"

        fun create(): EventAPIService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EventAPIService::class.java)
        }
    }
}