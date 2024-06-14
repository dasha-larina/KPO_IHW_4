package se.hse.ticket_order_service.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import se.hse.ticket_order_service.dto.OrderDTO
import se.hse.ticket_order_service.service.OrderService
import org.springframework.web.bind.annotation.*
import se.hse.ticket_order_service.model.Order

@Tag(name = "Ticket Order", description = "Operations related to managing ticket orders, including creating and retrieving order details.")
@RestController
@RequestMapping("/api/order")
class OrderController(val orderService: OrderService) {

    @PostMapping("/create")
    @Operation(summary = "Create Order", description = "Creates a new order for a ticket purchase between two stations.")
    @ApiResponse(responseCode = "201", description = "Order created successfully", content = [Content(schema = Schema(implementation = String::class))])
    @ApiResponse(responseCode = "400", description = "Invalid data", content = [Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = String::class)))])
    @ApiResponse(responseCode = "401", description = "Invalid or expired JWT token", content = [Content(schema = Schema(implementation = String::class))])
    @ApiResponse(responseCode = "404", description = "Station not found", content = [Content(schema = Schema(implementation = String::class))])
    @ApiResponse(responseCode = "409", description = "From and to station cannot be the same", content = [Content(schema = Schema(implementation = String::class))])
    @ApiResponse(responseCode = "500", description = "Internal server error", content = [Content(schema = Schema(implementation = String::class))])
    fun createOrder(@RequestBody @Valid body: OrderDTO, request: HttpServletRequest) = orderService.createOrder(body, request)

    @GetMapping("/info/{orderId}")
    @Operation(summary = "Get Order Information", description = "Retrieves detailed information about a specific order by order ID.")
    @ApiResponse(responseCode = "200", description = "Order found and information returned", content = [Content(schema = Schema(implementation = Order::class))])
    @ApiResponse(responseCode = "400", description = "Invalid order ID", content = [Content(schema = Schema(implementation = String::class))])
    @ApiResponse(responseCode = "404", description = "Order not found", content = [Content(schema = Schema(implementation = String::class))])
    fun getOrder(@PathVariable orderId: Int) = orderService.getOrderInfo(orderId)
}
