package com.example.tarefas

import android.app.Application
import com.example.tarefas.notification.NotificacaoHelper


class TarefasApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificacaoHelper.criarCanal(this)
    }
}
