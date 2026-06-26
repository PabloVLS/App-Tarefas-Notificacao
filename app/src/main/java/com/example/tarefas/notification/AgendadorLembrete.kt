package com.example.tarefas.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.example.tarefas.model.Tarefa


object AgendadorLembrete {

    const val EXTRA_ID = "extra_id"
    const val EXTRA_TITULO = "extra_titulo"
    const val EXTRA_CATEGORIA = "extra_categoria"
    const val EXTRA_MENSAGEM = "extra_mensagem"

    fun agendar(context: Context, tarefa: Tarefa) {
        if (tarefa.horario <= System.currentTimeMillis()) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = criarPendingIntent(context, tarefa)

        val exatoPermitido =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()

        try {
            if (exatoPermitido) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, tarefa.horario, pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, tarefa.horario, pendingIntent
                )
            }
        } catch (e: SecurityException) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, tarefa.horario, pendingIntent
            )
        }
    }

    fun podeAgendarExato(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return true
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return alarmManager.canScheduleExactAlarms()
    }

    fun abrirConfiguracoesAlarmeExato(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.fromParts("package", context.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    fun cancelar(context: Context, tarefa: Tarefa) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(criarPendingIntent(context, tarefa))
    }

    private fun criarPendingIntent(context: Context, tarefa: Tarefa): PendingIntent {
        val intent = Intent(context, LembreteReceiver::class.java).apply {
            putExtra(EXTRA_ID, tarefa.id)
            putExtra(EXTRA_TITULO, tarefa.titulo)
            putExtra(EXTRA_CATEGORIA, tarefa.categoria)
            putExtra(EXTRA_MENSAGEM, tarefa.mensagem)
        }
        return PendingIntent.getBroadcast(
            context,
            tarefa.id, // requestCode único por tarefa
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
