package com.radiusagent.assignment.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ExclusionsModel : RealmObject {
    @PrimaryKey
    var exclusionId   : String? = null
    var facilityId    : String? = null
    var optionsId     : String? = null
}