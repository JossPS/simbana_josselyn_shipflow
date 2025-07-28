package com.puce.simbana_josselyn_shipflow.models.entities

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "package")
data class ShippingPackage(
    @Column(name = "tracking_id", unique = true)
    val trackingId: String,
    val type: String,
    val weight: Float,
    @Column(length = 50)
    val description: String,
    @Column(name = "city_from")
    val cityFrom: String,
    @Column(name = "city_to")
    val cityTo: String,
    var status: Status,
    @JsonProperty("estimated_delivery")
    val estimatedDelivery: LocalDateTime,

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL], orphanRemoval = true)
    var events: MutableList<PackageEvent> = mutableListOf()

): BaseEntity()

enum class Type(name: String){
    DOCUMENT("D"),
    SMALL_BOX("SB"),
    FRAGILE("F")
}
