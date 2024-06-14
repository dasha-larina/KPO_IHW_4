package se.hse.auth_service.repository

import org.springframework.data.jpa.repository.JpaRepository
import se.hse.auth_service.model.Session

interface SessionRepository: JpaRepository<Session, Int>
