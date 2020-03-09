package com.mapinspector.utils.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mapinspector.R
import com.mapinspector.enity.PlaceDTO

class Adapter(
    private var list: MutableList<PlaceDTO>
) : RecyclerView.Adapter<PlaceHolder>() {

    var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    interface OnItemClickListener {
        fun onDeleteClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return PlaceHolder(itemView = itemView, listener = mListener)
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

    fun swap(places: List<PlaceDTO>, position: Int) {
        val diffCallback = PlaceDiffCallback(this.list, places)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, places.size)

        this.list.clear()
        this.list.addAll(places)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateList(list: MutableList<PlaceDTO>) {
        this.list = list
        notifyDataSetChanged()
    }
}