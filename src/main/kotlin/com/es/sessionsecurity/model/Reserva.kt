package com.es.sessionsecurity.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name="reservas")
class Reserva (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long?,
    var destino: String,
    var origen: String,
    var fechaIda: LocalDateTime,
    var fechaRegreso: LocalDateTime,
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    var usuario: Usuario?
)