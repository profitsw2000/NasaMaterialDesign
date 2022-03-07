package ru.profitsw2000.nasamaterialdesign.representation.earth

import com.google.gson.annotations.SerializedName

data class Resource(
    @field:SerializedName("dataset") val dataset: String,
    @field:SerializedName("planet") val planet: String
)