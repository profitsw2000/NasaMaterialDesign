package ru.profitsw2000.nasamaterialdesign.ui.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityRecyclerBinding
import ru.profitsw2000.nasamaterialdesign.representation.TYPE_CLEANING
import ru.profitsw2000.nasamaterialdesign.representation.TYPE_LEARNING
import ru.profitsw2000.nasamaterialdesign.representation.TYPE_REST
import ru.profitsw2000.nasamaterialdesign.representation.ToDoData
import ru.profitsw2000.nasamaterialdesign.ui.*
import ru.profitsw2000.nasamaterialdesign.ui.adapters.RecyclerActivityAdapter

class RecyclerActivity : AppCompatActivity() {

    lateinit var binding: ActivityRecyclerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getRealStyle(getCurrentTheme()))
        setContentView(R.layout.activity_recycler)
        binding = ActivityRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = arrayListOf(
            ToDoData(getString(R.string.rv_item_cleaning_text), 15, type = TYPE_CLEANING),
            ToDoData(getString(R.string.rv_item_cleaning_text), 11, type = TYPE_CLEANING),
            ToDoData(getString(R.string.rv_item_cleaning_text), 22, type = TYPE_CLEANING),
            ToDoData(getString(R.string.rv_item_learning_text), description = "Описание", type = TYPE_LEARNING),
            ToDoData(getString(R.string.rv_item_learning_text), description = "Описание", type = TYPE_LEARNING),
            ToDoData(getString(R.string.rv_item_learning_text), description = "Описание", type = TYPE_LEARNING),
            ToDoData(getString(R.string.rv_item_rest_text), type = TYPE_REST),
            ToDoData(getString(R.string.rv_item_rest_text), type = TYPE_REST),
            ToDoData(getString(R.string.rv_item_rest_text), type = TYPE_REST)
        )
        data.shuffle()
        val adapter = RecyclerActivityAdapter(object :OnItemClickListener{
            override fun onItemClick(data: ToDoData) {
                Toast.makeText(this@RecyclerActivity,"Это ${data.action}", Toast.LENGTH_SHORT).show()
            }
        })
        adapter.setData(data)
        binding.recyclerView.adapter = adapter
    }

    fun getCurrentTheme(): Int {
        val sharedPreferences = getSharedPreferences(KEY_SP, MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_CURRENT_THEME, -1)
    }

    fun getRealStyle(currentTheme: Int): Int {
        return when (currentTheme) {
            CosmicTheme -> R.style.Cosmic
            MoonTheme -> R.style.Moon
            MarsTheme -> R.style.Mars
            else -> 0
        }
    }
}