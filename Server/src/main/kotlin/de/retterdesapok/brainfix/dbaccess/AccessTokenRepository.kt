package de.retterdesapok.brainfix.dbaccess

import de.retterdesapok.brainfix.entities.AccessToken
import org.springframework.data.repository.CrudRepository

interface AccessTokenRepository : CrudRepository<AccessToken, Long> {
    public fun findByUserId(userId: Long) : AccessToken
    public fun findByToken(token: String) : AccessToken
}