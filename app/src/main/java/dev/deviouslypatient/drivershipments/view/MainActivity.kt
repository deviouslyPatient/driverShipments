package dev.deviouslypatient.drivershipments.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.deviouslypatient.drivershipments.databinding.ActivityMainBinding
import dev.deviouslypatient.drivershipments.viewmodel.DriverViewModel
import dev.deviouslypatient.drivershipments.viewmodel.ViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val driverViewModel: DriverViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ActivityMainBinding
    private lateinit var driverShipmentAdapter: DriverShipmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupUI()
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

    private fun setupObservers() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
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