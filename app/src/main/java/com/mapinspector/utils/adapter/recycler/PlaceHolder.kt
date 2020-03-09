package com.mapinspector.utils.adapter.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mapinspector.R
import com.mapinspector.enity.PlaceDTO
import kotlinx.android.synthetic.main.item_place.view.*

class PlaceHolder(itemView: View, private val listener: Adapter.OnItemClickListener?): RecyclerView.ViewHolder(itemView) {
    fun bind(
        data: Pair<Int, PlaceDTO>
    ) {
        itemView.tv_number.text = String.format(itemView.tv_number.resources.getString(R.string.number), data.first + 1)
        itemView.tv_placeName.text = data.second.placeName
        itemView.tv_coordinates.text = itemView.tv_coordinates.resources.getString(
            R.string.latLng,
            data.second.placeCoordinates.lat.toString(),
            data.second.placeCoordinates.lng.toString()
        )
        itemView.ib_delete.setOnClickListener {
            if (listener != null){
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION)
                    listener.onDeleteClick(position)
            }
        }
    }
}