package ru.profitsw2000.nasamaterialdesign.representation.mars

data class Rover(
    val id: Int,
    val landing_date: String,
    val launch_date: String,
    val name: String,
    val status: String
)