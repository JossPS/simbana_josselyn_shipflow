package com.puce.simbana_josselyn_shipflow.services

import com.puce.simbana_josselyn_shipflow.exceptions.exceptions.BusinessException
import com.puce.simbana_josselyn_shipflow.exceptions.exceptions.NotFoundException
import com.puce.simbana_josselyn_shipflow.mappers.PackageEventsMapper
import com.puce.simbana_josselyn_shipflow.mappers.PackageMapper
import com.puce.simbana_josselyn_shipflow.models.entities.PackageEvent
import com.puce.simbana_josselyn_shipflow.models.entities.Status
import com.puce.simbana_josselyn_shipflow.models.requests.PackageEventRequest
import com.puce.simbana_josselyn_shipflow.models.requests.PackageRequest
import com.puce.simbana_josselyn_shipflow.models.responses.PackageResponse
import com.puce.simbana_josselyn_shipflow.repositories.PackageRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class PackageService(
    private val packageRepository: PackageRepository,
    private val packageMapper: PackageMapper,
    private val packageEventsMapper: PackageEventsMapper
){
    fun save(request: PackageRequest): PackageResponse{
        if (request.cityFrom.equals(request.cityTo, ignoreCase = true)) {
            throw BusinessException("La ciudad de origen y destino no pueden ser iguales.")
        }
        if (request.description.length > 50) {
            throw BusinessException("La descripción no debe superar los 50 caracteres.")
        }

        val trackingId = UUID.randomUUID().toString().substring(0, 10)
        val now = LocalDateTime.now()
        val estimatedDelivery = now.plusDays(5)

        //En este crearemos la entidad del ShippingPackage sin eventos
        val shippingPackage = packageMapper.toEntity(request)
        shippingPackage.createdAt = now
        shippingPackage.updatedAt = now

        //Aqui crearemos el evento inicial con valor de  PENDING
        val initialEvent = PackageEvent(
            status = Status.PENDING,
            event = shippingPackage,
            comment = "Registro inicial"
        ).apply {
            createdAt = now
            updatedAt = now
        }
        shippingPackage.events += initialEvent

        val savedEntity = packageRepository.save(shippingPackage)

        return packageMapper.toResponse(savedEntity)
    }

    fun updateStatus(trackingId: String, request: PackageEventRequest): PackageResponse {
        val shippingPackage = packageRepository.findByTrackingId(trackingId)
            ?: throw NotFoundException("Paquete no encontrado.")

        val estadoActual = shippingPackage.status
        val nuevoEstado = Status.valueOf(request.status)

        if (estadoActual == Status.DELIVERED || estadoActual == Status.CANCELLED) {
            throw BusinessException("No se puede cambiar el estado de un paquete finalizado.")
        }

        when (estadoActual) {
            Status.PENDING -> {
                if (nuevoEstado != Status.IN_TRANSIT) {
                    throw BusinessException("El primer cambio debe ser a IN_TRANSIT.")
                }
            }
            Status.IN_TRANSIT -> {
                if (nuevoEstado != Status.DELIVERED &&
                    nuevoEstado != Status.ON_HOLD &&
                    nuevoEstado != Status.CANCELLED) {
                    throw BusinessException("Solo puede pasar a DELIVERED, ON_HOLD o CANCELLED.")
                }
            }
            Status.ON_HOLD -> {
                if (nuevoEstado != Status.IN_TRANSIT && nuevoEstado != Status.CANCELLED) {
                    throw BusinessException("Solo puede pasar a IN_TRANSIT o CANCELLED.")
                }
            }
            else -> {
                throw BusinessException("Transición de estado no permitida.")
            }
        }

        if (nuevoEstado == Status.DELIVERED) {
            val estuvoEnTransito = shippingPackage.events.any { it.status == Status.IN_TRANSIT }
            if (!estuvoEnTransito) {
                throw BusinessException("Solo puedes poner DELIVERED si el paquete ya estuvo en IN_TRANSIT.")
            }
        }

        shippingPackage.status = nuevoEstado
        shippingPackage.updatedAt = LocalDateTime.now()

        val evento = PackageEvent(
            status = nuevoEstado,
            event = shippingPackage,
            comment = request.comment
        ).apply {
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
        shippingPackage.events += evento

        val saved = packageRepository.save(shippingPackage)
        return packageMapper.toResponse(saved)
    }

    fun findByTrackingId(trackingId: String): PackageResponse {
        val entity = packageRepository.findByTrackingId(trackingId)
            ?: throw NotFoundException("Paquete no encontrado.")
        return packageMapper.toResponse(entity)
    }

    fun findAll(): List<PackageResponse> {
        return packageRepository.findAll()
            .map { packageMapper.toResponse(it) }
    }

}