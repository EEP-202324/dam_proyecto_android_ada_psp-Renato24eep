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
    val feedbacks by viewModel.feedbacks.observeAsState(emptyList())
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showEditFeedbackDialog by remember { mutableStateOf(false) } // Nueva bandera
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedFeedback by remember { mutableStateOf<Feedback?>(null) }

    LaunchedEffect(evento.id) {
        viewModel.fetchFeedbacksForEvent(evento.id)
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este evento?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteEvento(evento.id)
                        navController.navigateUp()
                        showConfirmDialog = false
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showConfirmDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

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
        Row {
            Button(onClick = { showEditDialog = true }) {
                Text(text = "Editar evento")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { showConfirmDialog = true }) {
                Text(text = "Eliminar evento")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Feedbacks", style = MaterialTheme.typography.titleMedium)
        LazyColumn(modifier = Modifier.fillMaxSize().weight(1f)) {
            items(feedbacks) { feedback ->
                FeedbackItem(feedback, viewModel, onEdit = { selectedFeedback = it; showEditFeedbackDialog = true })
            }
        }

        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Añadir feedback")
        }
    }

    if (showAddDialog) {
        AddFeedbackDialog(onDismiss = { showAddDialog = false }, onAdd = { feedback ->
            viewModel.addFeedback(evento, feedback)
            showAddDialog = false
        })
    }

    if (showEditFeedbackDialog) {
        selectedFeedback?.let { feedback ->
            EditFeedbackDialog(feedback = feedback, onDismiss = { showEditFeedbackDialog = false }, onSave = { updatedFeedback ->
                viewModel.updateFeedback(updatedFeedback)
                showEditFeedbackDialog = false
            })
        }
    }

    if (showEditDialog) {
        EditEventDialog(evento = evento, onDismiss = { showEditDialog = false }, onSave = { updatedEvento ->
            viewModel.updateEvento(updatedEvento)
            showEditDialog = false
        })
    }
}

@Composable
fun FeedbackItem(feedback: Feedback, viewModel: MainViewModel, onEdit: (Feedback) -> Unit) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este feedback?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteFeedback(feedback.id)
                        showConfirmDialog = false
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showConfirmDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = feedback.nombre, style = MaterialTheme.typography.titleMedium)
            Text(text = "Opinión: ${feedback.opinion}")
            Text(text = "Puntuación: ${feedback.puntuacion}")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = { onEdit(feedback) },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text(text = "Editar feedback")
                }
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text(text = "Borrar feedback")
                }
            }
        }
    }
}

@Composable
fun EditEventDialog(evento: Evento, onDismiss: () -> Unit, onSave: (Evento) -> Unit) {
    var titulo by remember { mutableStateOf(evento.titulo) }
    var hora by remember { mutableStateOf(evento.hora) }
    var localizacion by remember { mutableStateOf(evento.localizacion) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar evento") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Titulo") }
                )
                OutlinedTextField(
                    value = hora,
                    onValueChange = { hora = it },
                    label = { Text("Hora (HH:mm)") }
                )
                OutlinedTextField(
                    value = localizacion,
                    onValueChange = { localizacion = it },
                    label = { Text("Localizacion") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedEvento = evento.copy(titulo = titulo, hora = hora, localizacion = localizacion)
                onSave(updatedEvento)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun EditFeedbackDialog(feedback: Feedback, onDismiss: () -> Unit, onSave: (Feedback) -> Unit) {
    var nombre by remember { mutableStateOf(feedback.nombre) }
    var opinion by remember { mutableStateOf(feedback.opinion) }
    var puntuacion by remember { mutableStateOf(feedback.puntuacion) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar feedback") },
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
                    onValueChange = { puntuacion = it.toDoubleOrNull() ?: 0.0 },
                    label = { Text("Puntuación") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedFeedback = feedback.copy(nombre = nombre, opinion = opinion, puntuacion = puntuacion)
                onSave(updatedFeedback)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun AddFeedbackDialog(onDismiss: () -> Unit, onAdd: (Feedback) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var opinion by remember { mutableStateOf("") }
    var puntuacion by remember { mutableStateOf(0.0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Añadir opinion") },
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
                    onValueChange = { puntuacion = it.toDoubleOrNull() ?: 0.0 },
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
