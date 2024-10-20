package com.example.aplikasidicodingeventnavigationdanapi.dataAPI

import com.google.gson.annotations.SerializedName

data class Event(
    @field:SerializedName("listEvents") val listEvents: List<EventData> = listOf(),
    @field:SerializedName("event") val event: EventData? = null
)

data class EventData(
    @field:SerializedName("id") val id: String? = null,
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("summary") val summary: String? = null,
    @field:SerializedName("ownerName") val ownerName: String? = null,
    @field:SerializedName("description") val description: String? = null,
    @field:SerializedName("imageLogo") val imageLogo: String? = null,
    @field:SerializedName("category") val category: String? = null,
    @field:SerializedName("quota") val quota: Int? = null,
    @field:SerializedName("registrants") val registrants: Int? = null,
    @field:SerializedName("beginTime") val beginTime: String? = null,
    @field:SerializedName("link") val link: String? = null,
)
