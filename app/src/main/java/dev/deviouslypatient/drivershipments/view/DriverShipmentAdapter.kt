package dev.deviouslypatient.drivershipments.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.deviouslypatient.drivershipments.R
import dev.deviouslypatient.drivershipments.databinding.DriverItemBinding
import dev.deviouslypatient.drivershipments.model.Combination

class DriverShipmentAdapter(
    private val combinations: ArrayList<Combination>
): RecyclerView.Adapter<DriverShipmentAdapter.DriverShipmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverShipmentViewHolder {
        val binding = DriverItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DriverShipmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DriverShipmentViewHolder, position: Int) {
        holder.bind(combinations[position])
    }

    override fun getItemCount(): Int = combinations.size

    fun addData(data: Array<Combination>) {
        combinations.clear()
        combinations.addAll(data)
    }

    class DriverShipmentViewHolder(
        private val binding: DriverItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(combination: Combination) {
            binding.driverName.text = combination.driver.name
            val shipment = combination.shipment?.destination
            binding.shipmentDestination.text = binding.root.context.getString(R.string.shipment, shipment)
            val suitability = combination.suitibilityScore
            binding.suitabilityScore.text = binding.root.context.getString(R.string.suitability, suitability)
            binding.root.setOnClickListener {
                if (binding.shipmentDetails.visibility == View.VISIBLE) {
                    binding.shipmentDetails.visibility = View.GONE
                } else {
                    binding.shipmentDetails.visibility = View.VISIBLE
                }
            }
        }
    }
}