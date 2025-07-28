package com.puce.simbana_josselyn_shipflow.repositories

import com.puce.simbana_josselyn_shipflow.models.entities.ShippingPackage
import com.puce.simbana_josselyn_shipflow.models.entities.Status
import org.springframework.data.jpa.repository.JpaRepository

interface PackageRepository : JpaRepository<ShippingPackage, Long> {
    fun findByTrackingId(trackingId: String): ShippingPackage?

    fun findAllByStatus(status: Status): List<ShippingPackage>
}