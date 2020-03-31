package com.mapinspector.utils.adapter.recycler

import androidx.recyclerview.widget.DiffUtil
import com.mapinspector.db.enity.PlaceDTO

class PlaceDiffCallback(
    private val oldList: List<PlaceDTO>,
    private val newList: List<PlaceDTO>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].placeId == newList[newItemPosition].placeId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].placeName == newList[newItemPosition].placeName
    }
}