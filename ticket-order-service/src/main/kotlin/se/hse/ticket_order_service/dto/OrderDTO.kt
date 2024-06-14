package se.hse.ticket_order_service.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class OrderDTO(
    @Schema(defaultValue = "1")
    @field:NotNull(message = "From station ID required")
    @field:Min(1, message = "From station ID must be greater than 0")
    val fromStationId: Int,

    @Schema(defaultValue = "2")
    @field:NotNull(message = "To station ID required")
    @field:Min(1, message = "To station ID must be greater than 0")
    val toStationId: Int
)
