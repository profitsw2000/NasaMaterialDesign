package ru.profitsw2000.nasamaterialdesign.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityRecyclerItemCleaningBinding
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityRecyclerItemHeaderBinding
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityRecyclerItemLearningBinding
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityRecyclerItemRestBinding
import ru.profitsw2000.nasamaterialdesign.representation.TYPE_CLEANING
import ru.profitsw2000.nasamaterialdesign.representation.TYPE_HEADER
import ru.profitsw2000.nasamaterialdesign.representation.TYPE_LEARNING
import ru.profitsw2000.nasamaterialdesign.representation.ToDoData
import ru.profitsw2000.nasamaterialdesign.ui.recyclerview.OnItemClickListener

class RecyclerActivityAdapter (val onClickItemListener:OnItemClickListener):
    RecyclerView.Adapter<RecyclerActivityAdapter.BaseViewHolder>() {
    lateinit var listData: List<ToDoData>
    fun setData(listData:List<ToDoData>){
        this.listData = listData
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when(viewType){
            TYPE_CLEANING -> {
                val binding = ActivityRecyclerItemCleaningBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                CleaningViewHolder(binding.root)
            }
            TYPE_LEARNING -> {
                val binding = ActivityRecyclerItemLearningBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                LearnigViewHolder(binding.root)
            }
            TYPE_HEADER -> {
                val binding = ActivityRecyclerItemHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                HeaderViewHolder(binding.root)
            }
            else-> {
                val binding = ActivityRecyclerItemRestBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                RestViewHolder(binding.root)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemViewType(position: Int): Int {
        return listData[position].type
    }

    override fun getItemCount()=listData.size

    abstract class BaseViewHolder(view:View):RecyclerView.ViewHolder(view){
        abstract fun bind(data: ToDoData)
    }

    inner class CleaningViewHolder(view: View): BaseViewHolder(view){
        override fun bind(data: ToDoData){
            ActivityRecyclerItemCleaningBinding.bind(itemView).apply {
                tvActionName.text = data.action
                tvTime.text = root.context.getString(R.string.recycler_item_time_text) + data.time.toString() + ":00"
                ivCleaning.setOnClickListener {
                    onClickItemListener.onItemClick(data)
                }
            }
        }
    }

    inner class LearnigViewHolder(view: View): BaseViewHolder(view){
        override fun bind(data: ToDoData){
            ActivityRecyclerItemLearningBinding.bind(itemView).apply {
                tvActionName.text = data.action
                tvDescription.text = data.description
                ivLearning.setOnClickListener {
                    onClickItemListener.onItemClick(data)
                }
            }
        }
    }

    inner class RestViewHolder(view: View): BaseViewHolder(view){
        override fun bind(data: ToDoData){
            ActivityRecyclerItemRestBinding.bind(itemView).apply {
                tvActionName.text = data.action
                ivRest.setOnClickListener {
                    onClickItemListener.onItemClick(data)
                }
            }
        }
    }

    inner class HeaderViewHolder(view:View): BaseViewHolder(view){
        override fun bind(data: ToDoData){
            ActivityRecyclerItemHeaderBinding.bind(itemView).apply {
                tvName.text = data.action
                itemView.setOnClickListener {
                    onClickItemListener.onItemClick(data)
                }
            }
        }
    }
}