package com.puce.simbana_josselyn_shipflow.exceptions.handlers

import com.puce.simbana_josselyn_shipflow.exceptions.exceptions.BusinessException
import com.puce.simbana_josselyn_shipflow.exceptions.exceptions.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(mapOf("error" to ex.message.orEmpty()))

    @ExceptionHandler(BusinessException::class)
    fun handleBusiness(ex: BusinessException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("error" to ex.message.orEmpty()))

    @ExceptionHandler(Exception::class)
    fun handleGeneral(ex: Exception): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(mapOf("error" to (ex.message ?: "Error interno del servidor")))
}
