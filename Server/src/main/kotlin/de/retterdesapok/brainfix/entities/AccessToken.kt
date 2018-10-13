package de.retterdesapok.brainfix.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class AccessToken {
    @Id
    @GeneratedValue
    var id: Long? = null
    var userId: Long? = null
    var token: String? = null
    var valid: Boolean = false
}