package ru.profitsw2000.nasamaterialdesign.ui.recyclerview

import ru.profitsw2000.nasamaterialdesign.representation.ToDoData

interface OnItemClickListener {
    fun onItemClick(data: ToDoData)
}