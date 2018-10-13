package de.retterdesapok.brainfix.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import de.retterdesapok.brainfix.Utilities
import de.retterdesapok.brainfix.dbaccess.AccessTokenRepository
import de.retterdesapok.brainfix.dbaccess.NoteRepository
import de.retterdesapok.brainfix.dbaccess.UserRepository
import de.retterdesapok.brainfix.entities.AccessToken
import de.retterdesapok.brainfix.entities.Note
import de.retterdesapok.brainfix.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletResponse


@Controller
class MainController {

    @Autowired
    private val userRepository: UserRepository? = null
    @Autowired
    private val accessTokenRepository: AccessTokenRepository? = null
    @Autowired
    private val noteRepository: NoteRepository? = null

    @RequestMapping(path = arrayOf("/api/createtestuser"))
    @ResponseBody
    fun createAnton(): String {
        val anton = User()
        val passwordEncoder = BCryptPasswordEncoder()
        anton.passwordHash = passwordEncoder.encode("test")
        anton.email = "test@test.de"
        anton.isActive = true
        userRepository?.save(anton)

        val note = Note()
        note.content = "Testnotiz für #Anton"
        note.dateCreated = Utilities.getCurrentDateString()
        note.dateModified = Utilities.getCurrentDateString()
        note.dateSync = Utilities.getCurrentDateString()
        note.encryptionType = 0
        note.userId = anton.id!!
        note.uuid = UUID.randomUUID().toString()
        noteRepository?.save(note)

        val allUsers = userRepository?.findAll()

        val json = ObjectMapper().registerModule(KotlinModule())
        return json.writeValueAsString(allUsers)
    }

    @RequestMapping(value = "/test")
    @ResponseBody
    fun testPage(): String {
        return "Test"
    }


    @RequestMapping("/api/register")
    @ResponseBody
    fun doRegister(response: HttpServletResponse,
                   model: MutableMap<String, Any>,
                   @RequestParam("username") username: String?,
                   @RequestParam("password") password: String?): String {

        var userExists = false

        if (username != null) {
            val user = userRepository?.findByEmail(username)
            userExists = user != null
        }

        if (userExists) {
            response.status = HttpServletResponse.SC_NOT_ACCEPTABLE
            return "Username already taken. Brain too slow. Fix brain!"
        }

        val user = User()
        user.email = username!!
        val passwordEncoder = BCryptPasswordEncoder()
        user.passwordHash = passwordEncoder.encode(password)
        user.isActive = true
        userRepository?.save(user)

        val accessToken = AccessToken()
        accessToken.userId = user.id!!
        val createdToken = UUID.randomUUID().toString()
        accessToken.token = createdToken
        accessToken.valid = true
        accessTokenRepository?.save(accessToken)

        response.status = HttpServletResponse.SC_OK
        return createdToken
    }

    @RequestMapping("/api/requestToken")
    @ResponseBody
    fun doLogin(response: HttpServletResponse,
                model: MutableMap<String, Any>,
                @RequestParam("username") username: String?,
                @RequestParam("password") password: String?): String {

        var userExists = false
        var passwordCorrect = false
        var user: User? = null

        if (username != null) {
            user = userRepository?.findByEmail(username)
            userExists = user != null
        }

        if (userExists) {
            val passwordEncoder = BCryptPasswordEncoder()

            if (passwordEncoder.matches(password, user?.passwordHash)) {
                passwordCorrect = true
            }
        }

        if (!userExists || !passwordCorrect) {
            response.status = HttpServletResponse.SC_BAD_REQUEST
            return "Password incorrect. Fix brain!";
        }

        val accessToken = AccessToken()
        accessToken.userId = user!!.id!!
        val createdToken = UUID.randomUUID().toString()
        accessToken.token = createdToken
        accessToken.valid = true
        accessTokenRepository?.save(accessToken)

        response.status = HttpServletResponse.SC_OK
        return createdToken
    }

    @RequestMapping("/api/getNotes")
    @ResponseBody
    fun getNotes(response: HttpServletResponse,
                 model: MutableMap<String, Any>,
                 @RequestParam("token") token: String?,
                 @RequestParam("lastSync") dateLastSync: String?): String {

        response.status = HttpServletResponse.SC_BAD_REQUEST

        if (token == null || token.length < 16) {
            return "No many parameter. Fix brain!";
        }

        val accessToken = accessTokenRepository?.findByToken(token)


        if (accessToken == null || !accessToken.valid) {
            return "This no valid token. Fix brain!";
        }

        val notes = noteRepository?.findAllByUserId(accessToken.userId);

        response.status = HttpServletResponse.SC_OK
        val json = ObjectMapper().registerModule(KotlinModule())
        return json.writeValueAsString(notes)
    }

    @RequestMapping("/api/setNotes")
    @ResponseBody
    fun setNotes(response: HttpServletResponse,
                 model: MutableMap<String, Any>,
                 @RequestParam("token") token: String?,
                 @RequestParam("jsonData") jsonData: String?): String {

        response.status = HttpServletResponse.SC_BAD_REQUEST

        if (token == null || token.length < 16) {
            return "More parameters! Bad brain. Fix brain.";
        }

        val accessToken = accessTokenRepository?.findByToken(token)

        if (accessToken == null || !accessToken.valid) {
            return "This no valid token. Fix brain!";
        }

        val notes = noteRepository?.findAllByUserId(accessToken.userId);

        response.status = HttpServletResponse.SC_OK
        val json = ObjectMapper().registerModule(KotlinModule())
        return json.writeValueAsString(notes)
    }

    @ResponseBody
    fun errorPage(model: MutableMap<String, Any>): String {
        return "error"
    }
}