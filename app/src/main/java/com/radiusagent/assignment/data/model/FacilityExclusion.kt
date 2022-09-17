package com.radiusagent.assignment.data.model

import com.google.gson.annotations.SerializedName


data class FacilityExclusion (

  @SerializedName("facilities" ) var facilities : ArrayList<Facilities>            = arrayListOf(),
  @SerializedName("exclusions" ) var exclusions : ArrayList<ArrayList<Exclusions>> = arrayListOf()

)