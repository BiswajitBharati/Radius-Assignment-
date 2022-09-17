package com.radiusagent.assignment.data.repository

import android.util.Log
import com.radiusagent.assignment.data.api.FacilityLocalDataSource
import com.radiusagent.assignment.data.api.FacilityNetworkDataSource
import com.radiusagent.assignment.data.model.*
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import retrofit2.Response

class FacilityRepository(private val facilityNetworkDataSource: FacilityNetworkDataSource,
                         private val facilityLocalDataSource: FacilityLocalDataSource) {
    companion object {
        private val TAG = FacilityRepository::class.java.name
    }

    suspend fun getNetworkFacilities(): Response<FacilityExclusion>? {
        Log.d(TAG, "getNetworkFacilities()")
        return facilityNetworkDataSource.getFacilities()
    }

    suspend fun insertLocalFacilities(facilityExclusion: FacilityExclusion) {
        Log.d(TAG, "updateLocalFacilities()")
        facilityExclusion.facilities.forEach {
            val facilitiesModel = convertToLocalFacilities(facilities = it, exclusionsItems = facilityExclusion.exclusions)
            facilityLocalDataSource.insertLocalFacilities(facilitiesModel = facilitiesModel)
        }
    }

    suspend fun getLocalFacilities() {
        Log.d(TAG, "getLocalFacilities()")
        facilityLocalDataSource.getLocalFacilities()
    }

    private fun convertToLocalFacilities(facilities: Facilities, exclusionsItems : ArrayList<ArrayList<Exclusions>>) : FacilitiesModel {
        Log.d(TAG, "convertToLocalFacilities()")
        val optionsList: RealmList<OptionsModel> = realmListOf()
        facilities.options.forEach {
            val optionModel = OptionsModel().apply {
                id         = it.id
                icon       = it.icon
                name       = it.name
                exclusions = getExclusionList(facId = facilities.facilityId, optId = it.id, exclusionsItems = exclusionsItems)
            }
            optionsList.add(optionModel)
        }
        return FacilitiesModel().apply {
            facilityId = facilities.facilityId
            name       = facilities.name
            options    = optionsList
        }
    }

    private fun getExclusionList(facId: String?,
                                 optId: String?,
                                 exclusionsItems : ArrayList<ArrayList<Exclusions>>) : RealmList<ExclusionsModel> {
        Log.d(TAG, "getExclusionList() == facId: $facId optId: $optId")
        val exclusionList: RealmList<ExclusionsModel> = realmListOf()
        if (null == facId || null == optId) return exclusionList
        exclusionsItems.forEachIndexed { itemIndex, exclusions : ArrayList<Exclusions> ->
            val keyIndex = exclusions.indexOfFirst { it.facilityId == facId && it.optionsId == optId }
            if (keyIndex != -1) {
                exclusions.forEachIndexed { index, exclusion ->
                    if (keyIndex != index)
                        exclusionList.add(
                            ExclusionsModel().apply {
                                exclusionId = "${itemIndex}_$index"
                                facilityId = exclusion.facilityId
                                optionsId  = exclusion.optionsId
                            }
                        )
                }
            }
        }
        Log.d(TAG, "getExclusionList() == exclusionList: ${exclusionList.size}")
        return exclusionList
    }
}