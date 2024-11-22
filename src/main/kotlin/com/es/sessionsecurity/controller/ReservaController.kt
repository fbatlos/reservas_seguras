package com.es.sessionsecurity.controller

import com.es.sessionsecurity.error.ErrorRespuesta
import com.es.sessionsecurity.error.exception.BadRequestException
import com.es.sessionsecurity.model.Reserva
import com.es.sessionsecurity.service.ReservaService
import com.es.sessionsecurity.service.SessionService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reservas")
class ReservaController {

    @Autowired
    private lateinit var reservaService: ReservaService

    @Autowired
    private lateinit var sessionService: SessionService

    /*
    OBTENER TODAS LAS RESERVAS POR EL NOMBRE DE USUARIO DE UN CLIENTE
     */
    @GetMapping("/{nombre}")
    fun getByNombreUsuario(
        @PathVariable nombre: String,
        request: HttpServletRequest
    ) : ResponseEntity<List<Reserva>?> {

        /*
        COMPROBAR QUE LA PETICIÓN ESTÁ CORRECTAMENTE AUTORIZADA PARA REALIZAR ESTA OPERACIÓN

        LLAMAR AL SERVICE PARA REALIZAR LA L.N. Y LA LLAMADA A LA BASE DE DATOS
         */
        // CÓDIGO AQUÍ

        //cogemos la cookie
        val cookie = request.cookies.find { c: Cookie -> c.name == "tokenSession" }
        val token = cookie?.value

        if (sessionService.checkToken(token)){
            val reserva = reservaService.getALlReserva(nombre)
            return ResponseEntity<List<Reserva>?>(reserva, HttpStatus.OK)
        }
        // RESPUESTA
        return ResponseEntity<List<Reserva>?>(null, HttpStatus.BAD_REQUEST); // cambiar null por las reservas

    }

    /*
    INSERTAR UNA NUEVA RESERVA
     */
    @PostMapping("/")
    fun insert(
        @RequestBody nuevaReserva: Reserva
    ) : ResponseEntity<Reserva?>{

        /*
        COMPROBAR QUE LA PETICIÓN ESTÁ CORRECTAMENTE AUTORIZADA PARA REALIZAR ESTA OPERACIÓN
         */
        // CÓDIGO AQUÍ

        /*
        LLAMAR AL SERVICE PARA REALIZAR LA L.N. Y LA LLAMADA A LA BASE DE DATOS
         */
        // CÓDIGO AQUÍ

        // RESPUESTA
        return ResponseEntity<Reserva?>(null, HttpStatus.CREATED); // cambiar null por la reserva
    }

}