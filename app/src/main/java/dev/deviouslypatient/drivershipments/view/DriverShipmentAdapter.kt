package dev.deviouslypatient.drivershipments.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.deviouslypatient.drivershipments.R
import dev.deviouslypatient.drivershipments.databinding.DriverItemBinding
import dev.deviouslypatient.drivershipments.model.Assignment

class DriverShipmentAdapter(
    private val assignments: ArrayList<Assignment>
): RecyclerView.Adapter<DriverShipmentAdapter.DriverShipmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverShipmentViewHolder {
        val binding = DriverItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DriverShipmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DriverShipmentViewHolder, position: Int) {
        holder.bind(assignments[position])
    }

    override fun getItemCount(): Int = assignments.size

    fun addData(data: List<Assignment>) {
        assignments.clear()
        assignments.addAll(data)
    }

    class DriverShipmentViewHolder(
        private val binding: DriverItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(assignment: Assignment) {
            binding.driverName.text = assignment.driver
            val context = binding.root.context
            val shipment = assignment.shipment ?: context.getString(R.string.no_shipment)
            binding.shipmentDestination.text = context.getString(R.string.shipment, shipment)
            val suitability = assignment.suitabilityScore
            binding.suitabilityScore.text = context.getString(R.string.suitability, suitability)
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