package com.eep.dam.android.feedbackapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                    val navController = rememberNavController()
                    NavGraph(navController, viewModel)
                }
            }
        }
        viewModel.fetchEventos()
    }
}

@Composable
fun NavGraph(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = "mainScreen") {
        composable("mainScreen") {
            MainScreen(navController, viewModel)
        }
        composable("eventDetail/{eventoId}") { backStackEntry ->
            val eventoId = backStackEntry.arguments?.getString("eventoId")?.toLongOrNull()
            val evento = viewModel.getEventoById(eventoId)
            if (evento != null) {
                EventDetailScreen(navController, evento, viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel) {
    val eventos by viewModel.eventos.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(eventos) {
        Log.d("MainScreen", "Eventos cargados: ${eventos.size}")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (eventos.isEmpty()) {
                Text(
                    text = "No events available",
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
                )
            } else {
                EventList(navController, eventos)
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text(text = "Añadir evento")
        }
    }

    if (showDialog) {
        AddEventDialog(onDismiss = { showDialog = false }, onAdd = {
            viewModel.addEvento(it)
            showDialog = false
        })
    }
}

@Composable
fun EventList(navController: NavHostController, eventos: List<Evento>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(bottom = 56.dp)) {
        items(eventos) { evento ->
            EventItem(navController, evento)
        }
    }
}

@Composable
fun EventItem(navController: NavHostController, evento: Evento) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("eventDetail/${evento.id}") },
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
        title = { Text(text = "Añadir evento") },
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
                val evento = Evento(0, titulo, hora, localizacion)
                onAdd(evento)
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
