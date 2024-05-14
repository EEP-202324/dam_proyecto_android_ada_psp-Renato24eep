package com.eep.dam.android.feedbackapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import com.eep.dam.android.feedbackapp.model.Evento
import com.eep.dam.android.feedbackapp.ui.theme.FeedbackAppTheme
import com.eep.dam.android.feedbackapp.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FeedbackAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen(viewModel)
                }
            }
        }
        viewModel.fetchEventos()
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val eventos by viewModel.eventos.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(eventos) {
        Log.d("MainScreen", "Eventos cargados: ${eventos.size}")
    }

    if (showDialog) {
        AddEventDialog(onDismiss = { showDialog = false }, onAdd = {
            viewModel.addEvento(it)
            showDialog = false
        })
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (eventos.isEmpty()) {
            Text(
                text = "No events available",
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
            )
        } else {
            EventList(eventos)
        }
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text(text = "Add Event")
        }
    }
}

@Composable
fun EventList(eventos: List<Evento>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(eventos.size) { index ->
            val evento = eventos[index]
            EventItem(evento)
        }
    }
}

@Composable
fun EventItem(evento: Evento) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = evento.titulo, style = MaterialTheme.typography.titleLarge)
            Text(text = "Hora: ${evento.hora}")
            Text(text = "Localización: ${evento.localizacion}")
        }
    }
}

@Composable
fun AddEventDialog(onDismiss: () -> Unit, onAdd: (Evento) -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var localizacion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add Event") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Title") }
                )
                OutlinedTextField(
                    value = hora,
                    onValueChange = { hora = it },
                    label = { Text("Time (HH:mm)") }
                )
                OutlinedTextField(
                    value = localizacion,
                    onValueChange = { localizacion = it },
                    label = { Text("Location") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val evento = Evento(0, titulo, hora, localizacion)
                onAdd(evento)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
