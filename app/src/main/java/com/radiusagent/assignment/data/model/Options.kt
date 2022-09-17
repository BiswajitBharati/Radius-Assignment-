package com.radiusagent.assignment.data.model

import com.google.gson.annotations.SerializedName


data class Options (

  @SerializedName("name" ) var name : String? = null,
  @SerializedName("icon" ) var icon : String? = null,
  @SerializedName("id"   ) var id   : String? = null

)