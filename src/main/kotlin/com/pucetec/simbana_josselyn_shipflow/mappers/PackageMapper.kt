package com.pucetec.simbana_josselyn_shipflow.mappers

import com.pucetec.simbana_josselyn_shipflow.models.entities.ShippingPackage
import com.pucetec.simbana_josselyn_shipflow.models.entities.Status
import com.pucetec.simbana_josselyn_shipflow.models.requests.PackageRequest
import com.pucetec.simbana_josselyn_shipflow.models.responses.PackageResponse
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class PackageMapper(
    private val packageEventsMapper: PackageEventsMapper
){
    fun toEntity(request: PackageRequest): ShippingPackage {
        return ShippingPackage(
            trackingId = request.trackingId,             // ← Toma todo del request
            type = request.type,
            weight = request.weight,
            description = request.description,
            cityFrom = request.cityFrom,
            cityTo = request.cityTo,
            status = Status.valueOf(request.status),
            estimatedDelivery = request.estimatedDelivery,
            events = mutableListOf()
        )
    }
    fun toResponse(entity: ShippingPackage): PackageResponse {
        return PackageResponse(
            id = entity.id,
            trackingId = entity.trackingId,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            type = entity.type,
            weight = entity.weight,
            description = entity.description,
            cityFrom = entity.cityFrom,
            cityTo = entity.cityTo,
            status = entity.status.name,
            estimatedDelivery = entity.estimatedDelivery,
            events = entity.events.map { packageEventsMapper.toResponse(it) }
        )
    }
}