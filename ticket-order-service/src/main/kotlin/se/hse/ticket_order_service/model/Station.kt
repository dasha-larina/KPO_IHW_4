package se.hse.ticket_order_service.model

import jakarta.persistence.*

@Entity
@Table(name = "station")
class Station(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(name = "station", nullable = false)
    private val station: String
) {
    fun getStation() = station
}
