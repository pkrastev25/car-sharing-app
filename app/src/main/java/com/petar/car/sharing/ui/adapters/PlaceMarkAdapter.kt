package com.petar.car.sharing.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.petar.car.sharing.R
import com.petar.car.sharing.models.PlaceMarkModel
import kotlinx.android.synthetic.main.view_place_mark.view.*

class PlaceMarkAdapter : RecyclerView.Adapter<PlaceMarkAdapter.PlaceMarkViewHolder>() {

    private val items = arrayListOf<PlaceMarkModel>()

    fun setItems(items: List<PlaceMarkModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceMarkViewHolder {
        return PlaceMarkViewHolder(LayoutInflater
                .from(parent.context)
                .inflate(R.layout.view_place_mark, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: PlaceMarkViewHolder, position: Int) {
        items[position].apply {
            holder.name.text = this.name
            holder.address.text = this.address
            holder.engineType.text = this.engineType
            holder.fuel.text = this.fuel.toString()
            holder.exterior.text = this.exterior
            holder.interior.text = this.interior
        }
    }

    inner class PlaceMarkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.place_mark_name
        val address: TextView = view.place_mark_address
        val engineType: TextView = view.place_mark_engine_type_value
        val fuel: TextView = view.place_mark_fuel_label_value
        val exterior: TextView = view.place_mark_exterior_label_value
        val interior: TextView = view.place_mark_interior_label_value
    }
}