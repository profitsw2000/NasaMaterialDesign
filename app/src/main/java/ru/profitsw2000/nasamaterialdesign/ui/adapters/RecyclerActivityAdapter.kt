package ru.profitsw2000.nasamaterialdesign.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityRecyclerItemCleaningBinding
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityRecyclerItemHeaderBinding
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityRecyclerItemLearningBinding
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityRecyclerItemRestBinding
import ru.profitsw2000.nasamaterialdesign.representation.*
import ru.profitsw2000.nasamaterialdesign.ui.recyclerview.OnItemClickListener

class RecyclerActivityAdapter (val onClickItemListener:OnItemClickListener):
    RecyclerView.Adapter<RecyclerActivityAdapter.BaseViewHolder>(), ItemTouchHelperAdapter {
    private lateinit var listData: MutableList<Pair<ToDoData,Boolean>>

    fun setData(listData:MutableList<Pair<ToDoData,Boolean>>){
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
        return listData[position].first.type
    }

    override fun getItemCount()=listData.size

    fun appendItem(context: Context) {
        listData.add(generateData(context))
        notifyItemInserted(listData.size-1)
    }

    fun generateData(context: Context): Pair<ToDoData, Boolean> {
        val random = (0..23).random()

        when(random/8){
            0 -> return Pair(ToDoData(context.getString(R.string.rv_item_cleaning_text), random, type = TYPE_CLEANING),false)
            1 -> return Pair(ToDoData(context.getString(R.string.rv_item_learning_text), description = context.getString(R.string.rv_activity_description_text), type = TYPE_LEARNING),false)
            2 -> return Pair(ToDoData(context.getString(R.string.rv_item_rest_text), type = TYPE_REST),false)
        }
        return Pair(ToDoData(context.getString(R.string.rv_item_cleaning_text), random, type = TYPE_CLEANING),false)
    }

    fun generateCleaningData(context: Context): Pair<ToDoData, Boolean> {
        val random = (0..23).random()
        return Pair(ToDoData(context.getString(R.string.rv_item_cleaning_text), random, type = TYPE_CLEANING),false)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (toPosition != 0){
            listData.removeAt(fromPosition).apply {
                listData.add(toPosition, this)
            }
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    override fun onItemDismiss(position: Int) {
        if (position != 0){
            listData.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    abstract class BaseViewHolder(view:View):RecyclerView.ViewHolder(view){
        abstract fun bind(data: Pair<ToDoData, Boolean>)
    }

    inner class CleaningViewHolder(view: View): BaseViewHolder(view), ItemTouchHelperViewHolder{
        override fun bind(data: Pair<ToDoData, Boolean>){
            ActivityRecyclerItemCleaningBinding.bind(itemView).apply {
                tvActionName.text = data.first.action
                tvTime.text = root.context.getString(R.string.recycler_item_time_text) + data.first.time.toString() + ":00"
                ivCleaning.setOnClickListener {
                    onClickItemListener.onItemClick(data.first)
                }

                addItemImageView.setOnClickListener {
                    listData.add((layoutPosition + 1),generateCleaningData(root.context))
                    notifyItemInserted(layoutPosition + 1)
                }

                removeItemImageView.setOnClickListener {
                    listData.removeAt(layoutPosition)
                    notifyItemRemoved(layoutPosition)
                }

                moveItemUp.setOnClickListener {
                    layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                        listData.removeAt(currentPosition).apply {
                            listData.add(currentPosition - 1,this)
                        }
                        notifyItemMoved(currentPosition,currentPosition-1)
                    }
                }

                moveItemDown.setOnClickListener {
                    layoutPosition.takeIf { it < (listData.size - 1) }?.also { currentPosition ->
                        listData.removeAt(currentPosition).apply {
                            listData.add(currentPosition + 1,this)
                        }
                        notifyItemMoved(currentPosition,currentPosition + 1)
                    }
                }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.YELLOW)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    inner class LearnigViewHolder(view: View): BaseViewHolder(view), ItemTouchHelperViewHolder{
        override fun bind(data: Pair<ToDoData, Boolean>){
            ActivityRecyclerItemLearningBinding.bind(itemView).apply {
                tvActionName.text = data.first.action
                tvDescription.text = data.first.description

                ivLearning.setOnClickListener {
                    onClickItemListener.onItemClick(data.first)
                }

                tvDescription.visibility = if(listData[layoutPosition].second) View.VISIBLE else View.GONE

                itemView.setOnClickListener{
                    listData[layoutPosition] = listData[layoutPosition].let {
                        it.first to !it.second
                    }
                    notifyItemChanged(layoutPosition)
                }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.YELLOW)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    inner class RestViewHolder(view: View): BaseViewHolder(view), ItemTouchHelperViewHolder{
        override fun bind(data: Pair<ToDoData, Boolean>){
            ActivityRecyclerItemRestBinding.bind(itemView).apply {
                tvActionName.text = data.first.action
                ivRest.setOnClickListener {
                    onClickItemListener.onItemClick(data.first)
                }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.YELLOW)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    inner class HeaderViewHolder(view:View): BaseViewHolder(view){
        override fun bind(data: Pair<ToDoData, Boolean>){
            ActivityRecyclerItemHeaderBinding.bind(itemView).apply {
                tvName.text = data.first.action
                itemView.setOnClickListener {
                    onClickItemListener.onItemClick(data.first)
                }
            }
        }
    }
}