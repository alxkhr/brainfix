package de.retterdesapok.brainfix.dbaccess

import de.retterdesapok.brainfix.entities.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    public fun findByUsername(username: String) : User
}