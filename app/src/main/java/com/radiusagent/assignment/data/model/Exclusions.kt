package com.radiusagent.assignment.data.model

import com.google.gson.annotations.SerializedName


data class Exclusions (

  @SerializedName("facility_id" ) var facilityId : String? = null,
  @SerializedName("options_id"  ) var optionsId  : String? = null

)