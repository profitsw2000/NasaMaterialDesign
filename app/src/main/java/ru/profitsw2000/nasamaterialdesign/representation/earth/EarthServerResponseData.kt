package ru.profitsw2000.nasamaterialdesign.representation.earth

data class EarthServerResponseData(
    val date: String,
    val id: String,
    val resource: Resource,
    val service_version: String,
    val url: String
)