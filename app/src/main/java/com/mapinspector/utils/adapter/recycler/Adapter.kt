package com.mapinspector.utils.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mapinspector.R
import com.mapinspector.enity.PlaceDTO

class Adapter(
    private var list: List<PlaceDTO>
) : RecyclerView.Adapter<PlaceHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return PlaceHolder(itemView = itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PlaceHolder, position: Int) = holder.bind(
        Pair(
            position,
            PlaceDTO(
                list[position].placeId,
                list[position].placeName,
                list[position].placeCoordinates
            )
        )
    )

    fun updateList(list: List<PlaceDTO>) {
        this.list = list
        notifyDataSetChanged()
    }
}