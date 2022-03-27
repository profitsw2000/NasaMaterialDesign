package ru.profitsw2000.nasamaterialdesign.representation

import java.util.*

const val TYPE_CLEANING = 1
const val TYPE_LEARNING = 2
const val TYPE_REST = 3
const val TYPE_TITLE = 4

const val TYPE_PRIORITY_VERY_LOW = 1
const val TYPE_PRIORITY_LOW = 2
const val TYPE_PRIORITY_NORMAL = 3
const val TYPE_PRIORITY_HIGH = 4
const val TYPE_PRIORITY_VERY_HIGH = 5

data class ToDoData(
    val action: String = "Cleaning",
    val time: Int? = null,
    val description: String? = null,
    val type: Int = TYPE_CLEANING,
    val priority: Int? = null
)
