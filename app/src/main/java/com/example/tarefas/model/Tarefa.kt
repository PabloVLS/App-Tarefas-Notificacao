package com.example.tarefas.model


data class Tarefa(
    val id: Int,
    val titulo: String,
    val categoria: String,
    val mensagem: String,
    val horario: Long,
    val concluida: Boolean = false
)
