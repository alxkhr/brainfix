package de.retterdesapok.brainfix.dbaccess

import de.retterdesapok.brainfix.entities.Note
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface NoteRepository : CrudRepository<Note, Long> {
    public fun findAllByUserId(userId: Long) : Iterable<Note>
    @Query("from Note where userId=:userId AND dateSync > :dateFrom")
    public fun findAllByUserIdSinceDate(@Param("userId") userId: Long, @Param("dateFrom")date: String) : Iterable<Note>
    public fun findByUuid(uuid: String) : Note
}