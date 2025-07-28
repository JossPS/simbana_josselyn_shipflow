package com.puce.simbana_josselyn_shipflow.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class PackageEventRequest(
    var id: Long = 0,
    @JsonProperty("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @JsonProperty("udated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    val status: String,
    val comment: String? = null,

)
