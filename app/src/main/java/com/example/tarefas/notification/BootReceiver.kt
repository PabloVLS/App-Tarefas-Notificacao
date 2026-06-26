package com.example.tarefas.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.tarefas.data.RepositorioTarefas


class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val repositorio = RepositorioTarefas(context)
        val agora = System.currentTimeMillis()

        repositorio.carregarTarefas()
            .filter { !it.concluida && it.horario > agora }
            .forEach { AgendadorLembrete.agendar(context, it) }
    }
}
