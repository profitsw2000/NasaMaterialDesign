package ru.profitsw2000.nasamaterialdesign.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityRecyclerItemCleaningBinding
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityRecyclerItemLearningBinding
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityRecyclerItemRestBinding
import ru.profitsw2000.nasamaterialdesign.representation.TYPE_CLEANING
import ru.profitsw2000.nasamaterialdesign.representation.TYPE_LEARNING
import ru.profitsw2000.nasamaterialdesign.representation.ToDoData
import ru.profitsw2000.nasamaterialdesign.ui.recyclerview.OnItemClickListener

class RecyclerActivityAdapter (val onClickItemListener:OnItemClickListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var listData: List<ToDoData>
    fun setData(listData:List<ToDoData>){
        this.listData = listData
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_CLEANING -> {
                val binding = ActivityRecyclerItemCleaningBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                CleaningViewHolder(binding.root)
            }
            TYPE_LEARNING -> {
                val binding = ActivityRecyclerItemLearningBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                LearnigViewHolder(binding.root)
            }
            else-> {
                val binding = ActivityRecyclerItemRestBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                RestViewHolder(binding.root)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            TYPE_CLEANING -> {
                (holder as CleaningViewHolder).bind(listData[position])
            }
            TYPE_LEARNING -> {
                (holder as LearnigViewHolder).bind(listData[position])
            }
            else-> {
                (holder as RestViewHolder).bind(listData[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return listData[position].type
    }

    override fun getItemCount()=listData.size

    inner class CleaningViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(data: ToDoData){
            ActivityRecyclerItemCleaningBinding.bind(itemView).apply {
                tvActionName.text = data.action
                tvTime.text = "Время: " + data.time.toString() + ":00"
                ivCleaning.setOnClickListener {
                    onClickItemListener.onItemClick(data)
                }
            }
        }
    }

    inner class LearnigViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(data: ToDoData){
            ActivityRecyclerItemLearningBinding.bind(itemView).apply {
                tvActionName.text = data.action
                tvDescription.text = data.description
                ivLearning.setOnClickListener {
                    onClickItemListener.onItemClick(data)
                }
            }
        }
    }

    inner class RestViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(data: ToDoData){
            ActivityRecyclerItemRestBinding.bind(itemView).apply {
                tvActionName.text = data.action
                ivRest.setOnClickListener {
                    onClickItemListener.onItemClick(data)
                }
            }
        }
    }
}