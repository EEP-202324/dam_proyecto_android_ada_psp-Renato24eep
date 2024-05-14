package com.eep.dam.android.feedbackapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eep.dam.android.feedbackapp.api.RetrofitClient
import com.eep.dam.android.feedbackapp.api.ApiService
import com.eep.dam.android.feedbackapp.model.Evento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import android.util.Log // Importar para logs
import com.eep.dam.android.feedbackapp.model.Feedback

class MainViewModel : ViewModel() {

    private val _eventos = MutableLiveData<List<Evento>>()
    val eventos: LiveData<List<Evento>> get() = _eventos

    init {
        fetchEventos()
    }

    fun fetchEventos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("MainViewModel", "Fetching eventos...")
                val response = RetrofitClient.instance.create(ApiService::class.java).getEventos().awaitResponse()
                if (response.isSuccessful) {
                    _eventos.postValue(response.body())
                    Log.d("MainViewModel", "Eventos fetched: ${response.body()?.size}")
                } else {
                    Log.e("MainViewModel", "Error fetching eventos: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun addEvento(evento: Evento) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("MainViewModel", "Adding evento: $evento")
                val response = RetrofitClient.instance.create(ApiService::class.java).createEvento(evento).awaitResponse()
                if (response.isSuccessful) {
                    Log.d("MainViewModel", "Evento added successfully: ${response.body()}")
                    fetchEventos()  // Refresca la lista después de agregar
                } else {
                    Log.e("MainViewModel", "Error adding evento: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun getEventoById(id: Long?): Evento? {
        return _eventos.value?.find { it.id == id }
    }

    fun getFeedbacksForEvent(evento: Evento): MutableLiveData<List<Feedback>> {
        val feedbacks = MutableLiveData<List<Feedback>>()
        feedbacks.value = evento.feedbacks ?: emptyList()
        return feedbacks
    }

    fun addFeedback(evento: Evento, feedback: Feedback) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                feedback.evento = evento
                val response = RetrofitClient.instance.create(ApiService::class.java).createFeedback(feedback).awaitResponse()
                if (response.isSuccessful) {
                    Log.d("MainViewModel", "Feedback added successfully: ${response.body()}")
                    fetchEventos()  // Refresca la lista de eventos para incluir el nuevo feedback
                } else {
                    Log.e("MainViewModel", "Error adding feedback: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainViewModel", "Exception: ${e.message}")
            }
        }
    }
}
