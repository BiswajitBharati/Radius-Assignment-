package com.radiusagent.assignment.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.radiusagent.assignment.data.repository.FacilityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val facilityRepository: FacilityRepository) : ViewModel() {
    companion object {
        private val TAG = MainViewModel::class.java.name
    }

    var jobNwFacilities: Job? = null

    fun getNetworkFacilities() {
        Log.d(TAG, "getNetworkFacilities()")
        jobNwFacilities = CoroutineScope(Dispatchers.IO).launch {
            val result = facilityRepository.getNetworkFacilities()
            Log.d(TAG, "getNetworkFacilities() == isSuccess: ${result?.isSuccessful} response: ${result?.body()}")

            result?.body()?.let {
                facilityRepository.updateLocalFacilities(facilityExclusion = it)
                facilityRepository.getLocalFacilities()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared()")
        jobNwFacilities?.cancel()
    }
}