package ru.profitsw2000.nasamaterialdesign.representation

import java.util.*

const val TYPE_CLEANING = 1
const val TYPE_LEARNING = 2
const val TYPE_REST = 3
const val TYPE_HEADER = 4

data class ToDoData(
    val action: String = "Cleaning",
    val time: Int? = null,
    val description: String? = null,
    val type: Int = TYPE_CLEANING
)
