package com.radiusagent.assignment.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.radiusagent.assignment.BackgroundWorker
import com.radiusagent.assignment.R
import com.radiusagent.assignment.data.model.FacilitiesModel
import com.radiusagent.assignment.data.model.OptionsModel
import com.radiusagent.assignment.databinding.ActivityMainBinding
import com.radiusagent.assignment.ui.adapter.FacilitiesAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.ext.realmListOf
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.name
    }

    lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    lateinit var facilitiesAdapter: FacilitiesAdapter

    private val itemClickListener = object : ItemClickListener<String, OptionsModel?> {
        override fun onClickListener(key: String, item: OptionsModel?) {
            Log.d(TAG, "onClickListener()")
            mainViewModel.setExclusions(key = key, options = item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = this

        facilitiesAdapter = FacilitiesAdapter(exclusionsMap = mainViewModel.getExclusions(), listener = itemClickListener)
        facilitiesAdapter.submitList(mainViewModel.facilities.value ?: realmListOf())
        binding.facilitiesAdapter = facilitiesAdapter

        initObservers()
        setUpWorkManager()
    }

    private fun initObservers() {
        Log.d(TAG, "initObservers()")
        mainViewModel.isSuccess.observe(this, this::onFacilitiesResponse)
        mainViewModel.facilities.observe(this, this::onFacilitiesResult)
        mainViewModel.exclusionsUpdated.observe(this, this::onExclusionUpdate)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onExclusionUpdate(isUpdated: Boolean) {
        Log.d(TAG, "onExclusionUpdate() == isUpdated: $isUpdated")
        if (isUpdated) {
            facilitiesAdapter.notifyDataSetChanged()
            var options = ""
            mainViewModel.getExclusions().values.forEach {
                options += "${it.name} "
            }
            Toast.makeText(this, "Selected options : $options", Toast.LENGTH_SHORT).show()
        }
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

    private fun setUpWorkManager() {
        Log.d(TAG, "setUpWorkManager()")
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<BackgroundWorker>(15, TimeUnit.MINUTES)
                .setInitialDelay(15, TimeUnit.MINUTES).build()

        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(periodicWorkRequest)
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id).observeForever { workInfo ->
            if (null != workInfo) {
                Log.d(TAG, "setUpWorkManager() == Status changed to ${workInfo.state.isFinished}")
            }
        }
    }
}