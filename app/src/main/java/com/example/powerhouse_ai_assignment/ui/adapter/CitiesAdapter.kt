package com.example.powerhouse_ai_assignment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.powerhouse_ai_assignment.data.model.City
import com.example.powerhouse_ai_assignment.databinding.ItemCityBinding
import com.example.powerhouse_ai_assignment.ui.bottomsheet.BottomSheetDialog

class CitiesAdapter(private val fragmentManager: FragmentManager,private val cities: List<City>) : RecyclerView.Adapter<CitiesAdapter.ViewHolder>(){

    private lateinit var binding: ItemCityBinding

    inner class ViewHolder(private val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(city: City) {
            binding.cityText.text = city.name
            binding.root.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog(city.name)
                bottomSheetDialog.show(fragmentManager, bottomSheetDialog.tag)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemCityBinding.inflate(inflater,parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    override fun getItemCount(): Int {
        return cities.size
    }
}