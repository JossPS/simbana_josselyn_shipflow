package com.puce.simbana_josselyn_shipflow.models.entities

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "package_events")
data class PackageEvent(
    val status: Status,

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false) //fk -> Package(package_id)
    val event: ShippingPackage?= null,

    val comment: String? = null,

    ): BaseEntity()

enum class Status(val statusName: String){
    PENDING("PENDING"),
    IN_TRANSIT("IN_TRANSIT"),
    DELIVERED("DELIVERED"),
    ON_HOLD("ON_HOLD"),
    CANCELLED("CANCELLED")
}