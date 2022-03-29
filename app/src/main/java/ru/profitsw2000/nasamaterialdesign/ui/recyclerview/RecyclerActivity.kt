package ru.profitsw2000.nasamaterialdesign.ui.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityRecyclerBinding
import ru.profitsw2000.nasamaterialdesign.representation.*
import ru.profitsw2000.nasamaterialdesign.ui.*
import ru.profitsw2000.nasamaterialdesign.ui.adapters.RecyclerActivityAdapter
import java.util.*

class RecyclerActivity : AppCompatActivity() {

    lateinit var binding: ActivityRecyclerBinding
    private var notFirstLetter = false
    private lateinit var savedList: MutableList<Pair<ToDoData,Boolean>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getRealStyle(getCurrentTheme()))
        setContentView(R.layout.activity_recycler)
        binding = ActivityRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = arrayListOf(
            Pair(ToDoData(getString(R.string.rv_item_cleaning_text), 15, type = TYPE_CLEANING),false),
            Pair(ToDoData(getString(R.string.rv_item_learning_text), description = getString(R.string.rv_activity_description_text), type = TYPE_LEARNING),false)
        )
        data.shuffle()
        data.add(0, Pair(ToDoData(getString(R.string.header), type = TYPE_HEADER),false))
        val adapter = RecyclerActivityAdapter(object :OnItemClickListener{
            override fun onItemClick(data: ToDoData) {
                Toast.makeText(this@RecyclerActivity,"Это ${data.action}", Toast.LENGTH_SHORT).show()
            }
        })
        adapter.setData(data)
        binding.recyclerView.adapter = adapter

        binding.recyclerActivityFAB.setOnClickListener { adapter.appendItem(this@RecyclerActivity) }

        binding.inputEditText.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().lowercase(Locale.getDefault())

            if (query.isNotEmpty()) {
                if (!notFirstLetter) {
                    notFirstLetter = !notFirstLetter
                    savedList = adapter.getData()
                }

                val filteredList = filterWithQuery(query, savedList as ArrayList<Pair<ToDoData, Boolean>>)
                if (filteredList.isNotEmpty()) {
                    adapter.setData(filteredList.toMutableList())
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.findResult.visibility = View.GONE
                } else {
                    binding.recyclerView.visibility = View.GONE
                    binding.findResult.visibility = View.VISIBLE
                }
            }
            else {
                notFirstLetter = false
                adapter.setData(savedList)
            }
        }

        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(binding.recyclerView)
    }

    class ItemTouchHelperCallback(val recyclerActivityAdapter:RecyclerActivityAdapter):ItemTouchHelper.Callback(){
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(dragFlags,swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            from: RecyclerView.ViewHolder,
            to: RecyclerView.ViewHolder
        ): Boolean {
            recyclerActivityAdapter.onItemMove(from.adapterPosition,to.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            recyclerActivityAdapter.onItemDismiss(viewHolder.adapterPosition)
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            when(viewHolder){
                is RecyclerActivityAdapter.CleaningViewHolder -> {
                    if(actionState!=ItemTouchHelper.ACTION_STATE_IDLE) viewHolder.onItemSelected()
                }

                is RecyclerActivityAdapter.LearnigViewHolder -> {
                    if(actionState!=ItemTouchHelper.ACTION_STATE_IDLE) viewHolder.onItemSelected()
                }

                is RecyclerActivityAdapter.RestViewHolder -> {
                    if(actionState!=ItemTouchHelper.ACTION_STATE_IDLE) viewHolder.onItemSelected()
                }
            }
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            when(viewHolder){
                is RecyclerActivityAdapter.CleaningViewHolder -> {
                    viewHolder.onItemClear()
                }

                is RecyclerActivityAdapter.LearnigViewHolder -> {
                    viewHolder.onItemClear()
                }

                is RecyclerActivityAdapter.RestViewHolder -> {
                    viewHolder.onItemClear()
                }
            }
        }

    }

    private fun filterWithQuery(query: String, listData: ArrayList<Pair<ToDoData,Boolean>>)
                                :List<Pair<ToDoData,Boolean>>{
        var filteredList = arrayListOf<Pair<ToDoData,Boolean>>()

        for (data in listData) {
            if (data.first.action.contains(query, true) ||
                (!data.first.description.isNullOrEmpty() && data.first.description!!.contains(query, true))) filteredList.add(data)
        }

        return filteredList
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