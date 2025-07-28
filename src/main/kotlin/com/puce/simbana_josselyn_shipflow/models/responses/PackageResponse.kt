package com.puce.simbana_josselyn_shipflow.models.responses

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class PackageResponse (
    var id: Long = 0,
    @JsonProperty("tracking_id")
    val trackingId: String,
    @JsonProperty("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @JsonProperty("udated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    val type: String,
    val weight: Float,
    val description: String,
    @JsonProperty("city_from")
    val cityFrom: String,
    @JsonProperty("city_to")
    val cityTo: String,
    val status: String,
    @JsonProperty("estimated_delivery")
    val estimatedDelivery: LocalDateTime,

    val events: List<PackageEventResponse>,
)
