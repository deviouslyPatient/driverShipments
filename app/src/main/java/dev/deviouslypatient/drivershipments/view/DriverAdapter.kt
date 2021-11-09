package dev.deviouslypatient.drivershipments.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.deviouslypatient.drivershipments.databinding.DriverItemBinding

class DriverAdapter(
    private val drivers: ArrayList<String>
): RecyclerView.Adapter<DriverAdapter.DriverViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val binding = DriverItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DriverViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        holder.bind(drivers[position])
    }

    override fun getItemCount(): Int = drivers.size

    fun addData(data: Array<String>) {
        drivers.clear()
        drivers.addAll(data)
    }

    class DriverViewHolder(
        private val binding: DriverItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(driver: String) {
            binding.driverName.text = driver
        }
    }
}