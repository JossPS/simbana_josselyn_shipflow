package com.puce.simbana_josselyn_shipflow.services

import com.puce.simbana_josselyn_shipflow.mappers.PackageEventsMapper
import com.puce.simbana_josselyn_shipflow.mappers.PackageMapper
import com.puce.simbana_josselyn_shipflow.models.entities.ShippingPackage
import com.puce.simbana_josselyn_shipflow.models.requests.PackageRequest
import com.puce.simbana_josselyn_shipflow.models.responses.PackageResponse
import com.puce.simbana_josselyn_shipflow.repositories.PackageRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime

class PackageServiceTest {

    private val repo = Mockito.mock(PackageRepository::class.java)
    private val eventsMapper = Mockito.mock(PackageEventsMapper::class.java)
    private val mapper = Mockito.mock(PackageMapper::class.java)
    private val service = PackageService(repo, mapper, eventsMapper)

    @Test
    fun should_save_the_package() {
        val request = PackageRequest(
            trackingId = "A1",
            type = "DOCUMENT",
            weight = 1.0f,
            description = "Papeles",
            cityFrom = "Quito",
            cityTo = "Guayaquil",
            status = "PENDING",
            estimatedDelivery = LocalDateTime.now()
        )
        val entity = Mockito.mock(ShippingPackage::class.java)
        val responseEsperada = Mockito.mock(PackageResponse::class.java)

        Mockito.`when`(mapper.toEntity(request)).thenReturn(entity)
        Mockito.`when`(repo.save(entity)).thenReturn(entity)
        Mockito.`when`(mapper.toResponse(entity)).thenReturn(responseEsperada)

        val resultado = service.save(request)

        Assertions.assertEquals(responseEsperada, resultado)
        Mockito.verify(repo).save(entity)
    }
}