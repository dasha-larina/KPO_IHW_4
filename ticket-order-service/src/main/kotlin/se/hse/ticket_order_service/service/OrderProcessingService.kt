package se.hse.ticket_order_service.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import se.hse.ticket_order_service.model.Order
import se.hse.ticket_order_service.repository.OrderRepository
import java.util.*

@Service
class OrderProcessingService {

    @Autowired
    private val orderRepository: OrderRepository? = null

    @Scheduled(fixedRate = 60000)
    fun processOrders() {
        val ordersToCheck: List<Order> = orderRepository?.findByStatus(1) ?: return
        val random = Random()

        ordersToCheck.forEach { order ->
            val newStatus = if (random.nextBoolean()) 2 else 3
            order.setStatus(newStatus)
            try {
                orderRepository.save(order)
            } catch (ex: Exception) {
                println("Failed to save order with ID ${order.id}: ${ex.message}")
            }
        }
    }
}
