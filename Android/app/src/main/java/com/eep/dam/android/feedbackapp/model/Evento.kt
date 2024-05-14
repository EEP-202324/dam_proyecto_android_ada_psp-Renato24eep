package com.eep.dam.android.feedbackapp.model

data class Evento(
    val id: Long,
    val titulo: String,
    val hora: String,
    val localizacion: String,
    val feedbacks: List<Feedback>? = null
)
