package com.radiusagent.assignment.ui

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.radiusagent.assignment.R
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

        fun getPendingIntent(context: Context, requestCode: Int) : PendingIntent {
            return PendingIntent.getActivity(
                context,
                requestCode,
                Intent(context, MainActivity::class.java),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )
        }
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
}