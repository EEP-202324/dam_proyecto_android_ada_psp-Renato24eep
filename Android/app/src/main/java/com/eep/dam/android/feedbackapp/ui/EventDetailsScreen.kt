package com.eep.dam.android.feedbackapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.eep.dam.android.feedbackapp.model.Evento
import com.eep.dam.android.feedbackapp.model.Feedback
import com.eep.dam.android.feedbackapp.viewmodel.MainViewModel

@Composable
fun EventDetailScreen(navController: NavHostController, evento: Evento, viewModel: MainViewModel) {
    val feedbacks by viewModel.getFeedbacksForEvent(evento).observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = evento.titulo, style = MaterialTheme.typography.titleLarge)
        }

        Text(text = "Hora: ${evento.hora}")
        Text(text = "Localización: ${evento.localizacion}")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Feedbacks", style = MaterialTheme.typography.titleMedium)
        LazyColumn(modifier = Modifier.fillMaxSize().weight(1f)) {
            items(feedbacks) { feedback ->
                FeedbackItem(feedback)
            }
        }

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Añadir feedback")
        }
    }

    if (showDialog) {
        AddFeedbackDialog(onDismiss = { showDialog = false }, onAdd = { feedback ->
            viewModel.addFeedback(evento, feedback)
            showDialog = false
        })
    }
}

@Composable
fun FeedbackItem(feedback: Feedback) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = feedback.nombre, style = MaterialTheme.typography.titleMedium)
            Text(text = "Opinión: ${feedback.opinion}")
            Text(text = "Puntuación: ${feedback.puntuacion}")
        }
    }
}

@Composable
fun AddFeedbackDialog(onDismiss: () -> Unit, onAdd: (Feedback) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var opinion by remember { mutableStateOf("") }
    var puntuacion by remember { mutableStateOf(0.0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Añadir feedback") },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )
                OutlinedTextField(
                    value = opinion,
                    onValueChange = { opinion = it },
                    label = { Text("Opinión") }
                )
                OutlinedTextField(
                    value = puntuacion.toString(),
                    onValueChange = { puntuacion = it.toDouble() },
                    label = { Text("Puntuación") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val feedback = Feedback(0, nombre, opinion, puntuacion)
                onAdd(feedback)
            }) {
                Text("Añadir")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
