package ru.profitsw2000.nasamaterialdesign.representation.earth

import com.google.gson.annotations.SerializedName

data class EarthServerResponseData(
    @field:SerializedName("date") val date: String,
    @field:SerializedName("id") val id: String,
    @field:SerializedName("resource") val resource: Resource,
    @field:SerializedName("service_version") val service_version: String,
    @field:SerializedName("url") val url: String
)