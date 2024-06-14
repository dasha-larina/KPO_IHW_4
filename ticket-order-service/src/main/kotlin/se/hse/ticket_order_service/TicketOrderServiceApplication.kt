package se.hse.ticket_order_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TicketOrderServiceApplication

fun main(args: Array<String>) {
	runApplication<TicketOrderServiceApplication>(*args)
}
