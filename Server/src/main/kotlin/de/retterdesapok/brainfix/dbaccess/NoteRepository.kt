package de.retterdesapok.brainfix.dbaccess

import de.retterdesapok.brainfix.entities.Note
import org.springframework.data.repository.CrudRepository

interface NoteRepository : CrudRepository<Note, Long> {
    public fun findAllByUserId(userId: Long) : Iterable<Note>
    public fun findByUuid(uuid: String) : Note
}