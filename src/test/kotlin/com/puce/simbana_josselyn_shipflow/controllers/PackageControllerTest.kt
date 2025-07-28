package com.puce.simbana_josselyn_shipflow.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.puce.simbana_josselyn_shipflow.models.responses.PackageResponse
import com.puce.simbana_josselyn_shipflow.services.PackageService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@WebMvcTest(PackageController::class)
@Import(MockConfig::class)
class PackageControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var packageService: PackageService

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        objectMapper = ObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
    }

    val BASE_URL = "/api/packages"

    @Test
    fun should_return_package_when_get_by_tracking_id() {
        val trackingId = "A1"
        val packageResponse = PackageResponse(
            trackingId = trackingId,
            type = "DOCUMENT",
            weight = 1.0f,
            description = "Papeles",
            cityFrom = "Quito",
            cityTo = "Guayaquil",
            status = "PENDING",
            estimatedDelivery = LocalDateTime.of(2024, 7, 26, 14, 0),
            events = listOf()
        )

        `when`(packageService.findByTrackingId(trackingId)).thenReturn(packageResponse)

        val result = mockMvc.perform(get("$BASE_URL/$trackingId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.tracking_id").value(trackingId))
            .andExpect(jsonPath("$.type").value("DOCUMENT"))
            .andExpect(jsonPath("$.city_from").value("Quito"))
            .andExpect(jsonPath("$.city_to").value("Guayaquil"))
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andReturn()

        assertEquals(200, result.response.status)
    }
}


@TestConfiguration
class MockConfig {
    @Bean
    fun packageService(): PackageService = mock(PackageService::class.java)
}

