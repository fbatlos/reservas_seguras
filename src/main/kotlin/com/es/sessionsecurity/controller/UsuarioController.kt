package com.es.sessionsecurity.controller

import com.es.sessionsecurity.model.Reserva
import com.es.sessionsecurity.model.Usuario
import com.es.sessionsecurity.repository.SessionRepository
import com.es.sessionsecurity.service.SessionService
import com.es.sessionsecurity.service.UsuarioService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioController {
    @Autowired
    private lateinit var usuarioService: UsuarioService

    @Autowired
    private lateinit var sessionService: SessionService

    @PostMapping("/login")
    fun login(
       @RequestBody usuario: Usuario,
       respuesta: HttpServletResponse
    ) : ResponseEntity<Any> {


        // 1 LLAMAMOS AL SERVICE PARA REALIZAR LA L.N.
        val token : String = usuarioService.login(usuario)

        // 2 CREAR LA COOKIE
        /*
            a) Creamos la cookie -> Las cookies tienen un formato nombre:valor
            b) Hay una clase especializada en la gestión de cookies -> Clase Cookie
         */
        var cookie: Cookie = Cookie("tokenSession",token)

        // 3 INSERTAR LA COOKIE EN LA RESPUESTA
        /*
            a) Debemos incluir dentro de la respuesta la cookie que hemos creado en el punto anterior
            b) Esta cookie la almacenará automáticamente el cliente
         */
        respuesta.addCookie(cookie)

        // RESPUESTA
        return ResponseEntity(mapOf("message" to "login correcto"), HttpStatus.OK)
    }

    @PostMapping("/alta")
    fun alta(
        @RequestBody usuario: Usuario,
        //@PathVariable("nombre") nombre : String,
        request: HttpServletRequest
    ) : ResponseEntity<Usuario?> {

        //val cookie = request.cookies.find { c: Cookie -> c.name == "tokenSession" }
        //val token = cookie?.value

        //if (sessionService.checkToken(token,nombre)){
            val usuario = usuarioService.alta(usuario)
            return ResponseEntity<Usuario?>(usuario, HttpStatus.OK)
       // }
        // RESPUESTA
       // return ResponseEntity<Usuario?>(null, HttpStatus.BAD_REQUEST)

    }
}