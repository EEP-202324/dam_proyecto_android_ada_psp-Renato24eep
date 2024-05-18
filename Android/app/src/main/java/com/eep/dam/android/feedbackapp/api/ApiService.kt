package com.eep.dam.android.feedbackapp.api

import com.eep.dam.android.feedbackapp.model.Evento
import com.eep.dam.android.feedbackapp.model.Feedback
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("/api/evento")
    fun getEventos(): Call<List<Evento>>

    @POST("/api/evento")
    fun createEvento(@Body evento: Evento): Call<Evento>

    @PUT("/api/evento/{id}")
    fun updateEvento(@Path("id") id: Long, @Body evento: Evento): Call<Evento>

    @DELETE("/api/evento/{eventoId}")
    fun deleteEvento(@Path("eventoId") eventoId: Long): Call<Void>

    @POST("/api/feedback")
    fun createFeedback(@Body feedback: Feedback): Call<Feedback>

    @PUT("/api/feedback/{id}")
    fun updateFeedback(@Path("id") id: Long, @Body feedback: Feedback): Call<Feedback>

    @DELETE("/api/feedback/{feedbackId}")
    fun deleteFeedback(@Path("feedbackId") feedbackId: Long): Call<Void>

    @GET("/api/evento/{eventoId}")
    fun getEventoById(@Path("eventoId") eventoId: Long): Call<Evento>

    @GET("/api/feedback/evento/{eventoId}")
    fun getFeedbacksForEvent(@Path("eventoId") eventoId: Long): Call<List<Feedback>>
}
