package com.radiusagent.assignment.data.api

import com.radiusagent.assignment.data.model.FacilityExclusion
import retrofit2.Response
import retrofit2.http.GET

interface FacilityService {

    @GET("iranjith4/ad-assignment/db")
    suspend fun getFacilities(): Response<FacilityExclusion>?
}