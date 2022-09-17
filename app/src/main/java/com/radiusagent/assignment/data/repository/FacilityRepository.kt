package com.radiusagent.assignment.data.repository

import android.util.Log
import com.radiusagent.assignment.data.api.FacilityNetworkDataSource
import com.radiusagent.assignment.data.model.FacilityExclusion
import retrofit2.Response

class FacilityRepository(private val facilityNetworkDataSource: FacilityNetworkDataSource) {
    companion object {
        private val TAG = FacilityRepository::class.java.name
    }

    suspend fun getNetworkFacilities(): Response<FacilityExclusion>? {
        Log.d(TAG, "getNetworkFacilities()")
        return facilityNetworkDataSource.getFacilities()
    }
}