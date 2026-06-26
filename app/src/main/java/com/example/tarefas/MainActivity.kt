package com.example.tarefas

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tarefas.notification.AgendadorLembrete
import com.example.tarefas.ui.TarefaViewModel
import com.example.tarefas.ui.TelaListaTarefas
import com.example.tarefas.ui.theme.TarefasTheme

class MainActivity : ComponentActivity() {

    private val pedirNotificacao =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* resultado ignorado */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pedirNotificacao.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (!AgendadorLembrete.podeAgendarExato(this)) {
            AgendadorLembrete.abrirConfiguracoesAlarmeExato(this)
        }

        setContent {
            val viewModel: TarefaViewModel = viewModel()
            val temaEscuro by viewModel.temaEscuro.collectAsStateWithLifecycle()

            TarefasTheme(temaEscuro = temaEscuro) {
                TelaListaTarefas(viewModel = viewModel)
            }
        }
    }
}
