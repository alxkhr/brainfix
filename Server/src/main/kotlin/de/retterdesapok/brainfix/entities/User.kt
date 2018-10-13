package de.retterdesapok.brainfix.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User {
    @Id
    @GeneratedValue
    var id: Long? = null
    var username: String = ""
    var passwordHash: String = ""
    var isActive: Boolean = false
}