package com.radiusagent.assignment.data.model

import com.google.gson.annotations.SerializedName


data class Facilities (

  @SerializedName("facility_id" ) var facilityId : String?            = null,
  @SerializedName("name"        ) var name       : String?            = null,
  @SerializedName("options"     ) var options    : ArrayList<Options> = arrayListOf()

)