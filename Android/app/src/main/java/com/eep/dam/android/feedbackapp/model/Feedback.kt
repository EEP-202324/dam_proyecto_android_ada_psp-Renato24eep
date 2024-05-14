package com.eep.dam.android.feedbackapp.model

data class Feedback(
    val id: Long,
    val nombre: String,
    val opinion: String,
    val puntuacion: Double,
    val evento: Evento
)
