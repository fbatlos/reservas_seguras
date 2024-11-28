package com.es.sessionsecurity.service

import com.es.sessionsecurity.model.Reserva
import com.es.sessionsecurity.repository.ReservaRepository
import com.es.sessionsecurity.repository.UsuarioRepository
import com.es.sessionsecurity.util.CipherUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ReservaService {

    @Autowired
    private lateinit var reservaRepository: ReservaRepository

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository


    fun getALlReserva(nombre:String) : List<Reserva>{
        return reservaRepository.findByUsuario_Nombre(nombre)
    }


    fun insert(nuevaReserva: Reserva , token : String) : Reserva{

        val nombre = CipherUtils.decrypt(token,CipherUtils.constKey)

        val usuario = usuarioRepository.findByNombre(nombre)

        nuevaReserva.usuario = usuario.get()

        return reservaRepository.save(nuevaReserva)
    }


}