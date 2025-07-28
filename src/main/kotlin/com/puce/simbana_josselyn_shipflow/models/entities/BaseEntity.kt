package com.puce.simbana_josselyn_shipflow.models.entities

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PreUpdate
import org.intellij.lang.annotations.Identifier
import java.time.LocalDateTime


@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "udated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @PreUpdate
    protected fun update() {
        updatedAt = LocalDateTime.now()
    }
}
