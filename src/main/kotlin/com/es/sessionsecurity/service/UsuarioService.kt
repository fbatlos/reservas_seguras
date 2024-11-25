package com.es.sessionsecurity.service

import com.es.sessionsecurity.error.exception.BadRequestException
import com.es.sessionsecurity.error.exception.NotFoundException
import com.es.sessionsecurity.model.Session
import com.es.sessionsecurity.model.Usuario
import com.es.sessionsecurity.repository.SessionRepository
import com.es.sessionsecurity.repository.UsuarioRepository
import com.es.sessionsecurity.util.CipherUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@Service
class UsuarioService {

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository
    @Autowired
    private lateinit var sessionRepository: SessionRepository

    fun login(userLogin: Usuario) : String {

        // COMPROBACIÓN DE LOS CAMPOS DEL OBJETO USERLOGIN
        if(userLogin.password.isBlank() || userLogin.nombre.isBlank()) {
            throw BadRequestException("Los campos nombre y password son obligatorios")
        }

        // COMPROBAR CREDENCIALES
        // 1 Obtener el usuario de la base de datos
        var userBD: Usuario = usuarioRepository
            .findByNombre(userLogin.nombre)
            .orElseThrow{NotFoundException("El usuario proporcionado no existe en BDD")}



        // 2 Compruebo nombre y pass
        if(userBD.password == CipherUtils.encrypt(userLogin.password,CipherUtils.constKey)) {
            // 3 GENERAR EL TOKEN
            var token: String = ""
            token = CipherUtils.encrypt(userBD.nombre, CipherUtils.constKey)

            // 4 Comprobamos y CREAR UNA SESSION
            val s: Session
            val sevice = sessionRepository.findByToken(token =token)

            if (sevice.isEmpty) {
                s = Session(
                    null,
                    token,
                    userBD,
                    LocalDateTime.now().plusMinutes(3)
                )
                sessionRepository.save(s)
            }else{
                sevice.get().fechaExp = LocalDateTime.now().plusMinutes(3)
                sessionRepository.save(sevice.get())
            }
            // 5 INSERTAMOS EN BDD


            return token
        } else {
            // SI LA CONTRASEÑA NO COINCIDE, LANZAMOS EXCEPCIÓN
            throw NotFoundException("Las credenciales son incorrectas")
        }
    }


    fun alta(usuario: Usuario):Usuario {

        val usurioExiste = usuarioRepository.findByNombre(usuario.nombre)

        if (usurioExiste.isPresent) { throw BadRequestException("El usuario ya existe servidor") }

        val usuarioEncriptado = Usuario(
            id = usuario.id,
            nombre = usuario.nombre,
            password = CipherUtils.encrypt(usuario.password,CipherUtils.constKey),
        )

        usuarioRepository.save(usuarioEncriptado)
        return usuario
    }

}