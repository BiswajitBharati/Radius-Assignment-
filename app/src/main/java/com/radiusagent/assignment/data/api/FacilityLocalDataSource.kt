package com.radiusagent.assignment.data.api

import android.util.Log
import com.radiusagent.assignment.data.model.FacilitiesModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange

class FacilityLocalDataSource(private val realm: Realm) {
    companion object {
        private val TAG = FacilityLocalDataSource::class.java.name
    }

    suspend fun insertLocalFacilities(facilitiesModel: FacilitiesModel) {
        Log.d(TAG, "updateLocalFacilities() == facilitiesModel: $facilitiesModel")

        realm.write {
            copyToRealm(facilitiesModel)
        }
    }

    suspend fun getLocalFacilities() {
        Log.d(TAG, "getLocalFacilities()")

        realm.query<FacilitiesModel>().find()
            .asFlow().collect{ result: ResultsChange<FacilitiesModel> ->
                Log.d(TAG, "getAllFacilities() == result: ${result.list.size}")
                result.list.forEach {
                    Log.d(TAG, "getAllFacilities() == facility: ${it.facilityId} ${it.name}")
                    it.options.forEach { opt ->
                        Log.d(TAG, "getAllFacilities() == option: ${opt.id} ${opt.icon} ${opt.name}")
                        opt.exclusions.forEach { exclusion ->
                            Log.d(TAG, "getAllFacilities() == facility: ${it.facilityId} option: ${opt.id} exclusion: ${exclusion.exclusionId} ${exclusion.facilityId} ${exclusion.optionsId}")
                        }
                    }
                }
            }
    }
}