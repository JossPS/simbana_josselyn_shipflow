package com.puce.simbana_josselyn_shipflow.mappers


import com.puce.simbana_josselyn_shipflow.models.entities.PackageEvent
import com.puce.simbana_josselyn_shipflow.models.entities.ShippingPackage
import com.puce.simbana_josselyn_shipflow.models.entities.Status
import com.puce.simbana_josselyn_shipflow.models.requests.PackageEventRequest
import com.puce.simbana_josselyn_shipflow.models.responses.PackageEventResponse
import org.springframework.stereotype.Component


@Component
class PackageEventsMapper {

    fun toEntity(request: PackageEventRequest, shippingPackage: ShippingPackage): PackageEvent {
        return PackageEvent(
            status = Status.valueOf(request.status),
            event = shippingPackage,
            comment = request.comment
        ).apply {
            id = request.id
            createdAt = request.createdAt
            updatedAt = request.updatedAt
        }
    }
    fun toResponse(entity: PackageEvent): PackageEventResponse {
        return PackageEventResponse(
            id = entity.id,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            status = entity.status.name,
            comment = entity.comment
        )
    }
}