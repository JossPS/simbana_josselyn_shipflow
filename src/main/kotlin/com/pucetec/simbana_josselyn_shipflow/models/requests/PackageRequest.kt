package com.pucetec.simbana_josselyn_shipflow.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.pucetec.simbana_josselyn_shipflow.models.entities.Status
import java.time.LocalDateTime

data class PackageRequest (
    @JsonProperty("tracking_id")
    val trackingId: String,
    val type: String,
    val weight: Float,
    val description: String,
    @JsonProperty("city_from")
    val cityFrom: String,
    @JsonProperty("city_to")
    val cityTo: String,
    val status: String,
    @JsonProperty("estimated_delivery")
    val estimatedDelivery: LocalDateTime
)