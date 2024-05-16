package com.eep.dam.android.feedbackapp.api

import com.eep.dam.android.feedbackapp.model.Evento
import com.eep.dam.android.feedbackapp.model.Feedback
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/api/evento")
    fun getEventos(): Call<List<Evento>>

    @POST("/api/evento")
    fun createEvento(@Body evento: Evento): Call<Evento>

    @GET("/api/evento/{eventoId}")
    fun getEventoById(@Path("eventoId") eventoId: Long): Call<Evento>

    @DELETE("/api/evento/{eventoId}")
    fun deleteEvento(@Path("eventoId") eventoId: Long): Call<Void>

    @POST("/api/feedback")
    fun createFeedback(@Body feedback: Feedback): Call<Feedback>

    @GET("/api/feedback/evento/{eventoId}")
    fun getFeedbacksForEvent(@Path("eventoId") eventoId: Long): Call<List<Feedback>>

    @DELETE("/api/feedback/{feedbackId}")
    fun deleteFeedback(@Path("feedbackId") feedbackId: Long): Call<Void>
}
