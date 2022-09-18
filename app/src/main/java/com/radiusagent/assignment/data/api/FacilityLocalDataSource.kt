package com.radiusagent.assignment.data.api

import android.util.Log
import com.radiusagent.assignment.data.model.ExclusionsModel
import com.radiusagent.assignment.data.model.FacilitiesModel
import com.radiusagent.assignment.data.model.OptionsModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow

class FacilityLocalDataSource(private val realm: Realm) {
    companion object {
        private val TAG = FacilityLocalDataSource::class.java.name
    }

    suspend fun insertLocalFacilities(facilitiesModel: FacilitiesModel) {
        Log.d(TAG, "insertLocalFacilities() == facilitiesModel: $facilitiesModel")

        realm.write {
            copyToRealm(facilitiesModel)
        }
    }

    suspend fun updateLocalFacilities(facilitiesModel: FacilitiesModel) {
        Log.d(TAG, "updateLocalFacilities() == facilitiesModel: $facilitiesModel")

        facilitiesModel.facilityId?.let { facId ->
            realm.query<FacilitiesModel>("facilityId = $0", facId)
                .first().find()?.also { facModel ->
                    realm.write {
                        findLatest(facModel)?.apply {
                            name    = facilitiesModel.name
                            options = facilitiesModel.options
                        }
                    }
                } ?: insertLocalFacilities(facilitiesModel = facilitiesModel)
        }
    }

    suspend fun deleteLocalFacility(facilitiesModel: FacilitiesModel) {
        Log.d(TAG, "deleteLocalFacility() == facilitiesModel: $facilitiesModel")
        facilitiesModel.facilityId?.let { facId ->
            realm.write {
                val facilitiesQuery = this.query<FacilitiesModel>("facilityId = $0", facId)
                delete(facilitiesQuery)

                facilitiesModel.options.forEach { opt ->
                    val optionsModelQuery = this.query<OptionsModel>("id = $0", opt.id)
                    delete(optionsModelQuery)

                    opt.exclusions.forEach { exc ->
                        val exclusionsModelQuery = this.query<ExclusionsModel>("exclusionId = $0", exc.exclusionId)
                        delete(exclusionsModelQuery)
                    }
                }
            }
        }
    }

    suspend fun deleteLocalFacilities() {
        Log.d(TAG, "deleteLocalFacilities()")
        realm.write {
            val facilitiesQuery = this.query<FacilitiesModel>()
            delete(facilitiesQuery)

            val optionsModelQuery = this.query<OptionsModel>()
            delete(optionsModelQuery)

            val exclusionsModelQuery = this.query<ExclusionsModel>()
            delete(exclusionsModelQuery)
        }
    }

    fun getLocalFacilities() : Flow<ResultsChange<FacilitiesModel>> {
        Log.d(TAG, "getLocalFacilities()")

        return realm.query<FacilitiesModel>().find().asFlow()
    }
}