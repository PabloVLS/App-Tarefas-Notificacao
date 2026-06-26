package com.example.tarefas.data

import android.content.Context
import com.example.tarefas.model.Tarefa
import org.json.JSONArray
import org.json.JSONObject


class RepositorioTarefas(context: Context) {

    private val prefs =
        context.applicationContext.getSharedPreferences("tarefas_prefs", Context.MODE_PRIVATE)

    // ----------------------- Tarefas -----------------------

    fun carregarTarefas(): List<Tarefa> {
        val json = prefs.getString(CHAVE_TAREFAS, null) ?: return emptyList()
        val lista = mutableListOf<Tarefa>()
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            lista.add(
                Tarefa(
                    id = obj.getInt("id"),
                    titulo = obj.getString("titulo"),
                    categoria = obj.optString("categoria", ""),
                    mensagem = obj.optString("mensagem", ""),
                    horario = obj.getLong("horario"),
                    concluida = obj.optBoolean("concluida", false)
                )
            )
        }
        return lista
    }

    fun salvarTarefas(tarefas: List<Tarefa>) {
        val array = JSONArray()
        tarefas.forEach { t ->
            val obj = JSONObject().apply {
                put("id", t.id)
                put("titulo", t.titulo)
                put("categoria", t.categoria)
                put("mensagem", t.mensagem)
                put("horario", t.horario)
                put("concluida", t.concluida)
            }
            array.put(obj)
        }
        prefs.edit().putString(CHAVE_TAREFAS, array.toString()).apply()
    }

    /** Gera um id incremental e único para cada nova tarefa. */
    fun proximoId(): Int {
        val proximo = prefs.getInt(CHAVE_PROXIMO_ID, 1)
        prefs.edit().putInt(CHAVE_PROXIMO_ID, proximo + 1).apply()
        return proximo
    }

    // ----------------------- Tema -----------------------

    var temaEscuro: Boolean
        get() = prefs.getBoolean(CHAVE_TEMA_ESCURO, false)
        set(valor) = prefs.edit().putBoolean(CHAVE_TEMA_ESCURO, valor).apply()

    private companion object {
        const val CHAVE_TAREFAS = "tarefas"
        const val CHAVE_PROXIMO_ID = "proximo_id"
        const val CHAVE_TEMA_ESCURO = "tema_escuro"
    }
}
