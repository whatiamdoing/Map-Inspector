package com.mapinspector.utils.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mapinspector.R
import com.mapinspector.enity.PlaceDTO

class Adapter(
    private var list: List<PlaceDTO>, private val onClickListener:(PlaceDTO) -> Unit
) : RecyclerView.Adapter<PlaceHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return PlaceHolder(itemView = itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PlaceHolder, position: Int) {
        holder.bind(
            list[position]
        ) {
            removeItem(it)
        }
    }

    private fun removeItem(place: PlaceDTO) {
        onClickListener(place)
        val newList = this.list.filter { it.placeId != place.placeId }
        val diffCallback = PlaceDiffCallback(this.list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        this.list = newList
    }

    fun updateList(firstList: List<PlaceDTO>) {
        this.list = firstList
        notifyDataSetChanged()
    }
}