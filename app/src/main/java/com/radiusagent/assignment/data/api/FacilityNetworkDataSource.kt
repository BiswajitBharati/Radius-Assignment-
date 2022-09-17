package com.radiusagent.assignment.data.api

import android.util.Log
import com.radiusagent.assignment.data.model.FacilityExclusion
import retrofit2.Response

class FacilityNetworkDataSource(private val facilityService: FacilityService) {
    companion object {
        private val TAG = FacilityNetworkDataSource::class.java.name
    }

    suspend fun getFacilities(): Response<FacilityExclusion>? {
        Log.d(TAG, "getFacilities()")
        return try {
            facilityService.getFacilities()
        } catch (error: Exception) {
            Log.e(TAG, "getFacilities() == Error: ${error.message}")
            null
        }
    }
}