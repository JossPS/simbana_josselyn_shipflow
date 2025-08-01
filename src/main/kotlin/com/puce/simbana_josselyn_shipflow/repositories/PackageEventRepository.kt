package com.puce.simbana_josselyn_shipflow.repositories

import com.puce.simbana_josselyn_shipflow.models.entities.PackageEvent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PackageEventRepository: JpaRepository<PackageEvent, Long> {}