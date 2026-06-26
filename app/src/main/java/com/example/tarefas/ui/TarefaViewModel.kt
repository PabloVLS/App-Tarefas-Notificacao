package com.example.tarefas.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tarefas.data.RepositorioTarefas
import com.example.tarefas.model.Tarefa
import com.example.tarefas.notification.AgendadorLembrete
import com.example.tarefas.notification.NotificacaoHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel que mantém o estado da tela: a lista de tarefas e a preferência
 * de tema. Centraliza as regras de negócio (cadastrar, concluir, remover) e
 * dispara o agendamento/cancelamento dos lembretes no AlarmManager.
 */
class TarefaViewModel(app: Application) : AndroidViewModel(app) {

    private val repositorio = RepositorioTarefas(app)

    private val _tarefas = MutableStateFlow(
        repositorio.carregarTarefas().sortedBy { it.horario }
    )
    val tarefas: StateFlow<List<Tarefa>> = _tarefas.asStateFlow()

    private val _temaEscuro = MutableStateFlow(repositorio.temaEscuro)
    val temaEscuro: StateFlow<Boolean> = _temaEscuro.asStateFlow()

    fun adicionarTarefa(titulo: String, categoria: String, mensagem: String, horario: Long) {
        val nova = Tarefa(
            id = repositorio.proximoId(),
            titulo = titulo,
            categoria = categoria,
            mensagem = mensagem,
            horario = horario
        )
        atualizarLista(_tarefas.value + nova)
        AgendadorLembrete.agendar(getApplication(), nova)
    }

    fun alternarConcluida(tarefa: Tarefa) {
        val atualizada = tarefa.copy(concluida = !tarefa.concluida)
        atualizarLista(_tarefas.value.map { if (it.id == tarefa.id) atualizada else it })

        // Tarefa concluída não precisa mais lembrar; reativar reagenda.
        if (atualizada.concluida) {
            AgendadorLembrete.cancelar(getApplication(), atualizada)
        } else {
            AgendadorLembrete.agendar(getApplication(), atualizada)
        }
    }

    fun removerTarefa(tarefa: Tarefa) {
        AgendadorLembrete.cancelar(getApplication(), tarefa)
        atualizarLista(_tarefas.value.filterNot { it.id == tarefa.id })
    }

    /**
     * Dispara uma notificação imediata, para testar/diagnosticar se o aparelho
     * está exibindo as notificações do app (útil também para a demonstração).
     */
    fun testarNotificacao() {
        NotificacaoHelper.mostrar(
            getApplication(),
            id = 999_999,
            titulo = "Notificação de teste",
            categoria = "Teste",
            mensagem = "Se você está vendo isto, as notificações estão funcionando! 🎉"
        )
    }

    fun definirTema(escuro: Boolean) {
        _temaEscuro.value = escuro
        repositorio.temaEscuro = escuro
    }

    private fun atualizarLista(lista: List<Tarefa>) {
        val ordenada = lista.sortedBy { it.horario }
        _tarefas.value = ordenada
        repositorio.salvarTarefas(ordenada)
    }
}
