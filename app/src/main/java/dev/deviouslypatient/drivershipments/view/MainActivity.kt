package dev.deviouslypatient.drivershipments.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.deviouslypatient.drivershipments.data.DefaultDataRepository
import dev.deviouslypatient.drivershipments.data.JsonDataService
import dev.deviouslypatient.drivershipments.databinding.ActivityMainBinding
import dev.deviouslypatient.drivershipments.model.DefaultSuitabilityEngine
import dev.deviouslypatient.drivershipments.viewmodel.DriverViewModel
import dev.deviouslypatient.drivershipments.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var driverViewModel: DriverViewModel
    private lateinit var driverShipmentAdapter: DriverShipmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupUI()
        setupViewModels()
        setupObservers()
    }

    private fun setupUI() {
        driverShipmentAdapter = DriverShipmentAdapter(arrayListOf())
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = driverShipmentAdapter
        }
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this, linearLayoutManager.orientation)
        )
    }

    //todo inject the viewmodelfactory
    private fun setupViewModels() {
        driverViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                DefaultDataRepository(JsonDataService(applicationContext)),
                JsonDataService(applicationContext),
                DefaultSuitabilityEngine()))
            .get(DriverViewModel::class.java)
    }

    private fun setupObservers() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
//        driverViewModel
//            .getDrivers()
//            .observe(this, {
//                binding.progressBar.visibility = View.GONE
//                binding.recyclerView.visibility = View.VISIBLE
//                driverShipmentAdapter.addData(it)
//                driverShipmentAdapter::notifyDataSetChanged
//            })
        driverViewModel
            .getCombinations()
            .observe(this, {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                driverShipmentAdapter.addData(it)
                driverShipmentAdapter::notifyDataSetChanged
            })
    }
}