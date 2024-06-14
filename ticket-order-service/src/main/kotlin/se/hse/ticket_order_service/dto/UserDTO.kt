package se.hse.ticket_order_service.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class UserDTO(
    @JsonProperty("userId")
    val id: Int,
    val nickname: String,
    val email: String
)
