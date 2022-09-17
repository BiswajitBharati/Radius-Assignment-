package com.radiusagent.assignment.data.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class OptionsModel : RealmObject {
    @PrimaryKey
    var id            : String? = null
    var name          : String? = null
    var icon          : String? = null
    var exclusions    : RealmList<ExclusionsModel> = realmListOf()
}