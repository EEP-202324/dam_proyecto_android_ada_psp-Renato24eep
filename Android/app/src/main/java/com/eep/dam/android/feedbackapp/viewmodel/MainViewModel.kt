package com.eep.dam.android.feedbackapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eep.dam.android.feedbackapp.api.RetrofitClient
import com.eep.dam.android.feedbackapp.api.ApiService
import com.eep.dam.android.feedbackapp.model.Evento
import com.eep.dam.android.feedbackapp.model.Feedback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import android.util.Log

class MainViewModel : ViewModel() {

    private val _eventos = MutableLiveData<List<Evento>>()
    val eventos: LiveData<List<Evento>> get() = _eventos

    private val _feedbacks = MutableLiveData<List<Feedback>>()
    val feedbacks: LiveData<List<Feedback>> get() = _feedbacks

    init {
        fetchEventos()
    }

    fun fetchEventos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("MainViewModel", "Fetching eventos...")
                val response = RetrofitClient.instance.create(ApiService::class.java).getEventos().awaitResponse()
                if (response.isSuccessful) {
                    Log.d("MainViewModel", "Eventos fetched: ${response.body()?.size}")
                    _eventos.postValue(response.body())
                } else {
                    Log.e("MainViewModel", "Error fetching eventos: ${response.errorBody()?.string()}")
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
                    fetchEventos()
                } else {
                    Log.e("MainViewModel", "Error adding evento: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun updateEvento(evento: Evento) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.create(ApiService::class.java).updateEvento(evento.id, evento).awaitResponse()
                if (response.isSuccessful) {
                    Log.d("MainViewModel", "Evento updated successfully: ${response.body()}")
                    fetchEventos()
                } else {
                    Log.e("MainViewModel", "Error updating evento: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun deleteEvento(eventoId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("MainViewModel", "Deleting evento: $eventoId")
                val response = RetrofitClient.instance.create(ApiService::class.java).deleteEvento(eventoId).awaitResponse()
                if (response.isSuccessful) {
                    Log.d("MainViewModel", "Evento deleted successfully")
                    fetchEventos()
                } else {
                    Log.e("MainViewModel", "Error deleting evento: ${response.errorBody()?.string()}")
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

    fun fetchFeedbacksForEvent(eventoId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("MainViewModel", "Fetching feedbacks for evento: $eventoId")
                val response = RetrofitClient.instance.create(ApiService::class.java).getFeedbacksForEvent(eventoId).awaitResponse()
                if (response.isSuccessful) {
                    Log.d("MainViewModel", "Feedbacks fetched: ${response.body()?.size}")
                    _feedbacks.postValue(response.body())
                } else {
                    Log.e("MainViewModel", "Error fetching feedbacks: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun addFeedback(evento: Evento, feedback: Feedback) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                feedback.evento = evento
                val response = RetrofitClient.instance.create(ApiService::class.java).createFeedback(feedback).awaitResponse()
                if (response.isSuccessful) {
                    Log.d("MainViewModel", "Feedback added successfully: ${response.body()}")
                    fetchFeedbacksForEvent(evento.id)
                } else {
                    Log.e("MainViewModel", "Error adding feedback: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun updateFeedback(feedback: Feedback) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.create(ApiService::class.java).updateFeedback(feedback.id, feedback).awaitResponse()
                if (response.isSuccessful) {
                    Log.d("MainViewModel", "Feedback updated successfully: ${response.body()}")
                    feedback.evento?.id?.let { fetchFeedbacksForEvent(it) }
                } else {
                    Log.e("MainViewModel", "Error updating feedback: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun deleteFeedback(feedbackId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("MainViewModel", "Deleting feedback: $feedbackId")
                val response = RetrofitClient.instance.create(ApiService::class.java).deleteFeedback(feedbackId).awaitResponse()
                if (response.isSuccessful) {
                    Log.d("MainViewModel", "Feedback deleted successfully")
                    val updatedFeedbacks = _feedbacks.value?.filterNot { it.id == feedbackId } ?: emptyList()
                    _feedbacks.postValue(updatedFeedbacks)
                } else {
                    Log.e("MainViewModel", "Error deleting feedback: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainViewModel", "Exception: ${e.message}")
            }
        }
    }
}
