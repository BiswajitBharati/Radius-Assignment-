package com.radiusagent.assignment.data.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class FacilitiesModel : RealmObject {
    @PrimaryKey
    var facilityId : String?            = null
    var name       : String?            = null
    var options    : RealmList<OptionsModel> = realmListOf()
}