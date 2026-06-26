package com.example.tarefas.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tarefas.model.Tarefa
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaListaTarefas(viewModel: TarefaViewModel) {
    val tarefas by viewModel.tarefas.collectAsStateWithLifecycle()
    val temaEscuro by viewModel.temaEscuro.collectAsStateWithLifecycle()
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Tarefas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    // Botão para testar a notificação imediatamente
                    IconButton(onClick = { viewModel.testarNotificacao() }) {
                        Icon(
                            imageVector = Icons.Filled.NotificationsActive,
                            contentDescription = "Testar notificação",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    // Alternância de tema claro/escuro
                    Icon(
                        imageVector = if (temaEscuro) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                        contentDescription = "Tema atual",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Switch(
                        checked = temaEscuro,
                        onCheckedChange = { viewModel.definirTema(it) }
                    )
                    Spacer(Modifier.width(8.dp))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogo = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar tarefa")
            }
        }
    ) { innerPadding ->
        if (tarefas.isEmpty()) {
            EstadoVazio(Modifier.padding(innerPadding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tarefas, key = { it.id }) { tarefa ->
                    ItemTarefa(
                        tarefa = tarefa,
                        aoAlternarConcluida = { viewModel.alternarConcluida(tarefa) },
                        aoRemover = { viewModel.removerTarefa(tarefa) }
                    )
                }
            }
        }
    }

    if (mostrarDialogo) {
        DialogoNovaTarefa(
            aoFechar = { mostrarDialogo = false },
            aoSalvar = { titulo, categoria, mensagem, horario ->
                viewModel.adicionarTarefa(titulo, categoria, mensagem, horario)
                mostrarDialogo = false
            }
        )
    }
}

@Composable
private fun EstadoVazio(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.Notifications,
                contentDescription = null,
                modifier = Modifier.height(64.dp).width(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "Nenhuma tarefa cadastrada",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "Toque no botão + para criar seu primeiro lembrete.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ItemTarefa(
    tarefa: Tarefa,
    aoAlternarConcluida: () -> Unit,
    aoRemover: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = aoAlternarConcluida) {
                Icon(
                    imageVector = if (tarefa.concluida) Icons.Filled.CheckCircle
                    else Icons.Filled.RadioButtonUnchecked,
                    contentDescription = "Concluir",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tarefa.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (tarefa.concluida) TextDecoration.LineThrough else null
                )
                if (tarefa.categoria.isNotBlank()) {
                    Text(
                        text = tarefa.categoria,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Schedule,
                        contentDescription = null,
                        modifier = Modifier.height(16.dp).width(16.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = formatarDataHora(tarefa.horario),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (tarefa.mensagem.isNotBlank()) {
                    Text(
                        text = tarefa.mensagem,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = aoRemover) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Remover",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogoNovaTarefa(
    aoFechar: () -> Unit,
    aoSalvar: (titulo: String, categoria: String, mensagem: String, horario: Long) -> Unit
) {
    val context = LocalContext.current
    var titulo by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }
    val categorias = listOf("Beber Água", "Estudar", "Medicamento", "Exercício", "Compromisso")
    var categoria by remember { mutableStateOf(categorias.first()) }
    var expandido by remember { mutableStateOf(false) }

    // Horário padrão: daqui a 1 minuto.
    var horario by remember {
        mutableLongStateOf(
            Calendar.getInstance().apply { add(Calendar.MINUTE, 1) }.timeInMillis
        )
    }

    AlertDialog(
        onDismissRequest = aoFechar,
        title = { Text("Nova Tarefa") },
        confirmButton = {
            TextButton(
                onClick = {
                    if (titulo.isNotBlank()) {
                        aoSalvar(titulo.trim(), categoria, mensagem.trim(), horario)
                    }
                },
                enabled = titulo.isNotBlank()
            ) { Text("Salvar") }
        },
        dismissButton = {
            TextButton(onClick = aoFechar) { Text("Cancelar") }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título da tarefa") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                // Seletor de categoria
                ExposedDropdownMenuBox(
                    expanded = expandido,
                    onExpandedChange = { expandido = it }
                ) {
                    OutlinedTextField(
                        value = categoria,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoria") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = expandido,
                        onDismissRequest = { expandido = false }
                    ) {
                        categorias.forEach { opcao ->
                            DropdownMenuItem(
                                text = { Text(opcao) },
                                onClick = {
                                    categoria = opcao
                                    expandido = false
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = mensagem,
                    onValueChange = { mensagem = it },
                    label = { Text("Mensagem do lembrete (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        abrirSeletorDataHora(context, horario) { novoHorario ->
                            horario = novoHorario
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Schedule, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(formatarDataHora(horario))
                }
            }
        }
    )
}

/** Abre o seletor de data e, em seguida, o de hora. */
private fun abrirSeletorDataHora(
    context: Context,
    horarioAtual: Long,
    aoSelecionar: (Long) -> Unit
) {
    val calendario = Calendar.getInstance().apply { timeInMillis = horarioAtual }
    DatePickerDialog(
        context,
        { _, ano, mes, dia ->
            TimePickerDialog(
                context,
                { _, hora, minuto ->
                    val c = Calendar.getInstance()
                    c.set(ano, mes, dia, hora, minuto, 0)
                    c.set(Calendar.MILLISECOND, 0)
                    aoSelecionar(c.timeInMillis)
                },
                calendario.get(Calendar.HOUR_OF_DAY),
                calendario.get(Calendar.MINUTE),
                true
            ).show()
        },
        calendario.get(Calendar.YEAR),
        calendario.get(Calendar.MONTH),
        calendario.get(Calendar.DAY_OF_MONTH)
    ).show()
}

private fun formatarDataHora(horario: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale("pt", "BR"))
    return formato.format(Date(horario))
}
