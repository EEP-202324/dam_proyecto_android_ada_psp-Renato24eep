package com.eep.dam.android.feedbackapp.api

import com.eep.dam.android.feedbackapp.model.Evento
import com.eep.dam.android.feedbackapp.model.Feedback
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/api/evento")
    fun getEventos(): Call<List<Evento>>

    @POST("/api/evento")
    fun createEvento(@Body evento: Evento): Call<Evento>

    @POST("/api/feedback")
    fun createFeedback(@Body feedback: Feedback): Call<Feedback>
}
