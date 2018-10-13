package de.retterdesapok.brainfix.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import de.retterdesapok.brainfix.dbaccess.AccessTokenRepository
import de.retterdesapok.brainfix.dbaccess.UserRepository
import de.retterdesapok.brainfix.entities.AccessToken
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

    @RequestMapping(path = arrayOf("/createtestuser"))
    @ResponseBody
    fun createAnton(): String {
        var anton = User()
        anton.passwordHash = "\$2a\$10\$JW3zkNq1L/Iew613cPpAMuUfOQRrpTt8D0WtrXJAJeHGoB3GUnBrC"
        anton.email = "test@test.de"
        anton.isActive = true
        userRepository?.save(anton)

        var allUsers = userRepository?.findAll()

        val JSON = ObjectMapper().registerModule(KotlinModule())
        return JSON.writeValueAsString(allUsers)
    }

    @RequestMapping(value = "/test")
    @ResponseBody
    fun testPage(): String {
        return "Test"
    }



    @RequestMapping("/register")
    @ResponseBody
    fun doRegister(response : HttpServletResponse,
                model: MutableMap<String, Any>,
                @RequestParam("username") username: String?,
                @RequestParam("password") password: String?): String {

        var userExists = false

        if(username != null) {
            val user = userRepository?.findByEmail(username)
            userExists = user != null
        }

        if(userExists) {
            response.status = HttpServletResponse.SC_NOT_ACCEPTABLE
            return "Username already taken"
        }

        val user = User()
        user.email = username
        val passwordEncoder = BCryptPasswordEncoder()
        user.passwordHash = passwordEncoder.encode(password)
        user.isActive = true
        userRepository?.save(user)

        var accessToken = AccessToken()
        accessToken.userId = user.id
        val createdToken = UUID.randomUUID().toString()
        accessToken.token = createdToken
        accessToken.valid = true
        accessTokenRepository?.save(accessToken)

        response.status = HttpServletResponse.SC_OK
        return createdToken
    }

    @RequestMapping("/requestToken")
    @ResponseBody
    fun doLogin(response : HttpServletResponse,
                model: MutableMap<String, Any>,
                @RequestParam("username") username: String?,
                @RequestParam("password") password: String?): String {

        var userExists = false
        var passwordCorrect = false
        var user: User? = null

        if(username != null) {
            user = userRepository?.findByEmail(username)
            userExists = user != null
        }

        if(userExists) {
            val passwordEncoder = BCryptPasswordEncoder()

            if(passwordEncoder.matches(password, user?.passwordHash)) {
                passwordCorrect = true
            }
        }

        if(!userExists || !passwordCorrect) {
            response.status = HttpServletResponse.SC_BAD_REQUEST
            return "incorrect password, bitch";
        }

        var accessToken = AccessToken()
        accessToken.userId = user?.id
        val createdToken = UUID.randomUUID().toString()
        accessToken.token = createdToken
        accessToken.valid = true
        accessTokenRepository?.save(accessToken)

        response.status = HttpServletResponse.SC_OK
        return createdToken
    }

    @ResponseBody
    fun errorPage(model: MutableMap<String, Any>): String {
        return "error"
    }
}