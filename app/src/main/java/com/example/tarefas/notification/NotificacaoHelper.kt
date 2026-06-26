package com.example.tarefas.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.tarefas.MainActivity
import com.example.tarefas.R


object NotificacaoHelper {

    const val ID_CANAL = "lembretes_tarefas"

    fun criarCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                ID_CANAL,
                "Lembretes de Tarefas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificações dos lembretes das suas atividades"
                enableVibration(true)
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    /** Indica se o app tem permissão para postar notificações (Android 13+). */
    fun temPermissaoNotificacao(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
    }

    fun mostrar(
        context: Context,
        id: Int,
        titulo: String,
        categoria: String,
        mensagem: String
    ) {
        criarCanal(context)

        val tituloNotificacao =
            if (categoria.isNotBlank()) "$categoria • $titulo" else titulo
        val textoNotificacao =
            if (mensagem.isNotBlank()) mensagem else "Está na hora de: $titulo"

        val abrirApp = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingAbrir = PendingIntent.getActivity(
            context, id, abrirApp,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificacao = NotificationCompat.Builder(context, ID_CANAL)
            .setSmallIcon(R.drawable.ic_lembrete)
            .setContentTitle(tituloNotificacao)
            .setContentText(textoNotificacao)
            .setStyle(NotificationCompat.BigTextStyle().bigText(textoNotificacao))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingAbrir)
            .build()

        if (temPermissaoNotificacao(context)) {
            NotificationManagerCompat.from(context).notify(id, notificacao)
        }
    }
}
