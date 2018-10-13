package de.retterdesapok.brainfix.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Note {
    @Id
    @GeneratedValue
    var id: Long? = null
    var uuid: String? = null
    var userId: Long = -1
    var encryptionType: Long = 0
    var content: String? = null
    var dateCreated: String? = null
    var dateModified: String? = null
    var dateSync: String? = null
}