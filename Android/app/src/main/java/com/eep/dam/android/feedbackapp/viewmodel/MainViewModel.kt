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

class MainViewModel : ViewModel() {

    private val _eventos = MutableLiveData<List<Evento>>()
    val eventos: LiveData<List<Evento>> get() = _eventos

    init {
        fetchEventos()
    }

    fun fetchEventos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
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
                val response = RetrofitClient.instance.create(ApiService::class.java).createEvento(evento).awaitResponse()
                if (response.isSuccessful) {
                    fetchEventos()
                    Log.d("MainViewModel", "Evento added successfully")
                } else {
                    Log.e("MainViewModel", "Error adding evento: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainViewModel", "Exception: ${e.message}")
            }
        }
    }
}
