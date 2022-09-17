package com.radiusagent.assignment.di

import com.radiusagent.assignment.data.api.FacilityNetworkDataSource
import com.radiusagent.assignment.data.api.FacilityService
import com.radiusagent.assignment.data.api.ServiceProvider
import com.radiusagent.assignment.data.repository.FacilityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FacilitiesModule {

    @Provides
    @Singleton
    fun provideFacilityRepository(facilityNetworkDataSource: FacilityNetworkDataSource) : FacilityRepository {
        return FacilityRepository(facilityNetworkDataSource = facilityNetworkDataSource)
    }

    @Provides
    @Singleton
    fun provideFacilityNetworkDataSource(facilityService: FacilityService) : FacilityNetworkDataSource {
        return FacilityNetworkDataSource(facilityService = facilityService)
    }

    @Provides
    @Singleton
    fun provideFacilityService() : FacilityService {
        return ServiceProvider.createService(FacilityService::class.java)
    }
}