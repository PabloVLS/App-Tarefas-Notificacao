package com.example.tarefas.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class LembreteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra(AgendadorLembrete.EXTRA_ID, 0)
        val titulo = intent.getStringExtra(AgendadorLembrete.EXTRA_TITULO) ?: "Lembrete"
        val categoria = intent.getStringExtra(AgendadorLembrete.EXTRA_CATEGORIA) ?: ""
        val mensagem = intent.getStringExtra(AgendadorLembrete.EXTRA_MENSAGEM) ?: ""

        NotificacaoHelper.mostrar(context, id, titulo, categoria, mensagem)
    }
}
