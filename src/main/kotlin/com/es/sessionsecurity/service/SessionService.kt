package com.es.sessionsecurity.service

import com.es.sessionsecurity.error.exception.NotFoundException
import com.es.sessionsecurity.model.Session
import com.es.sessionsecurity.repository.SessionRepository
import com.es.sessionsecurity.util.CipherUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SessionService {

    @Autowired
    private lateinit var sessionRepository: SessionRepository

    fun checkToken(token:String? , nombre:String) : Boolean {
        if (token == null) return false
        //1º session asociada
        val session: Session

        //2º DesEncryptamos y comparamos el nombre

        val nombreEncrip = CipherUtils.encrypt(nombre, CipherUtils.constKey)

        if (token == nombreEncrip){
            session = sessionRepository.findByToken(token).orElseThrow{RuntimeException("Token invalido")}
            return session.fechaExp.isAfter(LocalDateTime.now())
        }else{
            throw Exception("Token invalido")
        }
        //Pr ultimo comprobamos la fecha

    }

}