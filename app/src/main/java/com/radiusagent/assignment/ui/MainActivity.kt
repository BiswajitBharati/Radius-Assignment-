package com.radiusagent.assignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.radiusagent.assignment.R
import com.radiusagent.assignment.data.model.Facilities
import com.radiusagent.assignment.data.model.FacilitiesModel
import com.radiusagent.assignment.data.model.OptionsModel
import com.radiusagent.assignment.databinding.ActivityMainBinding
import com.radiusagent.assignment.ui.adapter.FacilitiesAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.ext.realmListOf

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.name
    }

    lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    lateinit var facilitiesAdapter: FacilitiesAdapter

    val itemClickListener = object : ItemClickListener<OptionsModel> {
        override fun onClickListener(item: OptionsModel) {
            Toast.makeText(baseContext, "You click ${item.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = this

        facilitiesAdapter = FacilitiesAdapter(itemClickListener)
        facilitiesAdapter.submitList(mainViewModel.facilities.value ?: realmListOf())
        binding.facilitiesAdapter = facilitiesAdapter

        initObservers()
    }

    private fun initObservers() {
        Log.d(TAG, "initObservers()")
        mainViewModel.isSuccess.observe(this, this::onFacilitiesResponse)
        mainViewModel.facilities.observe(this, this::onFacilitiesResult)
    }

    private fun onFacilitiesResult(facilities: List<FacilitiesModel>?) {
        Log.d(TAG, "onFacilitiesResponse() == facilities size: ${facilities?.size}")
        facilitiesAdapter.submitList(facilities ?: realmListOf())
    }

    private fun onFacilitiesResponse(isSuccess: Boolean) {
        Log.d(TAG, "onFacilitiesResponse() == isSuccess: $isSuccess")
        val response = if (isSuccess) "Facilities fetched successfully!" else "Facilities fetch failed!"
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
    }
}