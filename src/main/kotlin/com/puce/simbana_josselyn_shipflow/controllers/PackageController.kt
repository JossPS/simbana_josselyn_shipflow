package com.puce.simbana_josselyn_shipflow.controllers


import com.puce.simbana_josselyn_shipflow.models.requests.PackageEventRequest
import com.puce.simbana_josselyn_shipflow.models.requests.PackageRequest
import com.puce.simbana_josselyn_shipflow.models.responses.PackageEventResponse
import com.puce.simbana_josselyn_shipflow.models.responses.PackageResponse
import com.puce.simbana_josselyn_shipflow.routes.Routes
import com.puce.simbana_josselyn_shipflow.services.PackageService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/packages")
class PackageController(
    private val service: PackageService,
){
    @PostMapping
    fun createPackage(
        @RequestBody request: PackageRequest,
    ): PackageResponse {
        return service.save(request)
    }

    @GetMapping(Routes.BY_ID)
    fun getByTrackingId(
        @PathVariable trackingId: String
    ): PackageResponse = service.findByTrackingId(trackingId)


    @GetMapping(Routes.HISTORY)
    fun getPackageHistory(
        @PathVariable trackingId: String
    ): List<PackageEventResponse> {
        val entity = service.findByTrackingId(trackingId)
        return entity.events
    }


    @PutMapping(Routes.STATUS)
    fun updatePackageStatus(
        @PathVariable trackingId: String,
        @RequestBody request: PackageEventRequest
    ): PackageResponse = service.updateStatus(trackingId, request)


    @GetMapping(Routes.ALL)
    fun getAll(): List<PackageResponse> = service.findAll()
}