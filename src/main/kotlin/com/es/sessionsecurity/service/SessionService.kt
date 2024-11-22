package com.es.sessionsecurity.service

import com.es.sessionsecurity.error.exception.NotFoundException
import com.es.sessionsecurity.repository.SessionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SessionService {

    @Autowired
    private lateinit var sessionRepository: SessionRepository

    fun checkToken(token:String?) : Boolean {
        if (token == null) return false
        //1º session asociada
        val session = sessionRepository.findByToken(token).orElseThrow{RuntimeException("Token invalido")}

        //Pr ultimo comprobamos la fecha
        return session.fechaExp.isAfter(LocalDateTime.now())
    }

}