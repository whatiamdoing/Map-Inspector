package com.mapinspector.utils.adapter.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mapinspector.R
import com.mapinspector.enity.PlaceDTO
import kotlinx.android.synthetic.main.item_place.view.*

class PlaceHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(
        data: PlaceDTO,
        onDeleteClickListener: (PlaceDTO) -> Unit,
        onItemClickListener: (PlaceDTO) -> Unit
        ) {
        itemView.tv_placeName.text = data.placeName
        itemView.tv_coordinates.text = itemView.tv_coordinates.resources.getString(
            R.string.latLng,
            data.placeCoordinates.lat.toString(),
            data.placeCoordinates.lng.toString()
        )
        itemView.ib_delete.setOnClickListener {
            onDeleteClickListener(data)
        }
        itemView.place_container.setOnClickListener {
            onItemClickListener(data)
        }
    }
}