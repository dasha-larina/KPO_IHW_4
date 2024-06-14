package se.hse.ticket_order_service.service

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import se.hse.ticket_order_service.dto.OrderDTO
import se.hse.ticket_order_service.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.client.RestTemplate
import se.hse.ticket_order_service.dto.UserDTO
import se.hse.ticket_order_service.model.Order
import se.hse.ticket_order_service.repository.StationRepository
import se.hse.ticket_order_service.util.JwtUtil

@Service
class OrderService(
    val orderRepository: OrderRepository,
    val stationRepository: StationRepository,
    val restTemplate: RestTemplate,
) {

    @Transactional
    fun createOrder(@RequestBody body: OrderDTO, request: HttpServletRequest): ResponseEntity<Any> {
        if (body.fromStationId == body.toStationId)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("From and to station cannot be the same")

        val fromStation = stationRepository.findStationById(body.fromStationId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("From station not found")

        val toStation = stationRepository.findStationById(body.toStationId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("To station not found")

        val jwtToken = JwtUtil.extractToken(request)

        if (jwtToken == null || !JwtUtil.validateToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired JWT token")
        }

        val userId = fetchUserIdFromAuthService(jwtToken)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User data not found based on the provided token.")

        val newOrder = Order(
            userId = userId,
            fromStation = fromStation,
            toStation = toStation
        )

        try {
            orderRepository.save(newOrder)
            return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully")
        } catch (ex: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
        }
    }

    fun getOrderInfo(@PathVariable orderId: Int): ResponseEntity<Any> {
        if (orderId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order ID must be greater than 0")
        }

        val order = orderRepository.findById(orderId).orElse(null)
        return if (order != null) {
            ResponseEntity.status(HttpStatus.OK).body(order)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found")
        }
    }

    private fun fetchUserIdFromAuthService(jwtToken: String): Int? {
        val headers = org.springframework.http.HttpHeaders()
        headers.setBearerAuth(jwtToken)

        val entity = HttpEntity<String>(headers)

        val response = restTemplate.exchange(
            "/api/auth/user",
            HttpMethod.GET,
            entity,
            UserDTO::class.java
        )

        return response.body?.id
    }
}
