package dev.deviouslypatient.drivershipments.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.deviouslypatient.drivershipments.data.DefaultDataRepository
import dev.deviouslypatient.drivershipments.data.JsonDataService
import dev.deviouslypatient.drivershipments.databinding.ActivityMainBinding
import dev.deviouslypatient.drivershipments.viewmodel.DriverViewModel
import dev.deviouslypatient.drivershipments.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var driverViewModel: DriverViewModel
    private lateinit var driverAdapter: DriverAdapter

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
        driverAdapter = DriverAdapter(arrayListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = driverAdapter
    }

    //todo inject the viewmodelfactory
    private fun setupViewModels() {
        driverViewModel = ViewModelProvider(
            this,
            ViewModelFactory(DefaultDataRepository(JsonDataService(applicationContext)))
        )
            .get(DriverViewModel::class.java)
    }

    private fun setupObservers() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        driverViewModel
            .getDrivers()
            .observe(this, {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                driverAdapter.addData(it)
                driverAdapter::notifyDataSetChanged
            })
    }
}