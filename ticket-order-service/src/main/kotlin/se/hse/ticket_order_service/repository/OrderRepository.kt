package se.hse.ticket_order_service.repository

import org.springframework.data.jpa.repository.JpaRepository
import se.hse.ticket_order_service.model.Order

interface OrderRepository : JpaRepository<Order, Int> {
    fun findByStatus(status: Int): List<Order>
}
