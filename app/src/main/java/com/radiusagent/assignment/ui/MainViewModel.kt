package com.radiusagent.assignment.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.radiusagent.assignment.data.model.FacilitiesModel
import com.radiusagent.assignment.data.model.OptionsModel
import com.radiusagent.assignment.data.repository.FacilityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.notifications.ResultsChange
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

    private var jobFacilities: Job? = null
    private var jobNwFacilities: Job? = null

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _facilities = MutableLiveData<List<FacilitiesModel>>()
    val facilities: LiveData<List<FacilitiesModel>> = _facilities

    private val _exclusionsUpdated = MutableLiveData<Boolean>()
    val exclusionsUpdated: LiveData<Boolean> = _exclusionsUpdated

    private val exclusionsMap: HashMap<String, OptionsModel> = hashMapOf()
    fun getExclusions() : HashMap<String, OptionsModel>{
        return exclusionsMap
    }

    fun setExclusions(key: String, options: OptionsModel?) {
        Log.d(TAG, "setExclusions() == key: $key options: $options")
        if (null == options) exclusionsMap.remove(key = key)
        else exclusionsMap[key] = options
        _exclusionsUpdated.value = true
    }

    init {
        Log.d(TAG, "init")
        getLocalFacilities()
        _exclusionsUpdated.value = false
    }

    private fun getLocalFacilities() {
        jobFacilities = CoroutineScope(Dispatchers.IO).launch {
            facilityRepository.getLocalFacilities().collect{ result: ResultsChange<FacilitiesModel> ->
                Log.d(TAG, "getAllFacilities() == result: ${result.list.size}")
                _facilities.postValue(result.list)
            }
        }
    }

    fun getNetworkFacilities() {
        Log.d(TAG, "getNetworkFacilities()")
        jobNwFacilities = CoroutineScope(Dispatchers.IO).launch {
            val result = facilityRepository.getNetworkFacilities()
            Log.d(TAG, "getNetworkFacilities() == isSuccess: ${result?.isSuccessful} response: ${result?.body()}")

            if (true == result?.isSuccessful) {
                result.body()?.let {
                    exclusionsMap.clear()
                    facilityRepository.updateLocalFacilities(facilityExclusion = it)
                }
            }
            _isSuccess.postValue(result?.isSuccessful ?: false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared()")
        jobFacilities?.cancel()
        jobNwFacilities?.cancel()
    }
}