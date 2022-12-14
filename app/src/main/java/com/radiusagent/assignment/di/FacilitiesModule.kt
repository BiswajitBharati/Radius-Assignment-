package com.radiusagent.assignment.di

import android.content.Context
import android.content.SharedPreferences
import com.radiusagent.assignment.RadiusApplication
import com.radiusagent.assignment.data.api.*
import com.radiusagent.assignment.data.model.ExclusionsModel
import com.radiusagent.assignment.data.model.FacilitiesModel
import com.radiusagent.assignment.data.model.OptionsModel
import com.radiusagent.assignment.data.repository.FacilityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FacilitiesModule {

    @Provides
    @Singleton
    fun provideFacilityRepository(facilityNetworkDataSource: FacilityNetworkDataSource,
                                  facilityLocalDataSource: FacilityLocalDataSource) : FacilityRepository {
        return FacilityRepository(facilityNetworkDataSource = facilityNetworkDataSource,
            facilityLocalDataSource = facilityLocalDataSource)
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

    @Provides
    @Singleton
    fun provideFacilityLocalDataSource(realm: Realm) : FacilityLocalDataSource {
        return FacilityLocalDataSource(realm = realm)
    }

    @Provides
    @Singleton
    fun provideRealm(configuration: RealmConfiguration) : Realm {
        return Realm.open(configuration = configuration)
    }

    @Provides
    @Singleton
    fun provideRealmConfiguration() : RealmConfiguration {
        return RealmConfiguration.Builder(schema = setOf(FacilitiesModel::class, OptionsModel::class, ExclusionsModel::class)).build()
    }

    @Provides
    @Singleton
    fun provideAppPreference(sharedPreferences: SharedPreferences) : AppPreferences {
        return AppPreferences(sharedPreferences = sharedPreferences)
    }

    @Provides
    @Singleton
    fun providePreference() : SharedPreferences {
        return RadiusApplication.getApplicationContext().getSharedPreferences("com.radiusagent.assignment.PREF", Context.MODE_PRIVATE)
    }
}