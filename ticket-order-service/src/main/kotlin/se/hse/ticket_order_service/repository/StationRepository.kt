package se.hse.ticket_order_service.repository

import org.springframework.data.jpa.repository.JpaRepository
import se.hse.ticket_order_service.model.Station

interface StationRepository: JpaRepository<Station, Int> {
    fun findStationById(id: Int): Station?
}
