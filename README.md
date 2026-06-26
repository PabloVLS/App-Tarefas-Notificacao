# 📋 Minhas Tarefas — Lista de Tarefas com Lembretes

Aplicativo móvel **Android** desenvolvido em **Kotlin + Jetpack Compose** que permite
cadastrar atividades do cotidiano (estudo, ingestão de água, exercícios, medicamentos,
compromissos pessoais, etc.) e receber **notificações locais no horário programado**,
mesmo com o aplicativo fechado. O app possui ainda **alternância entre Tema Claro e
Tema Escuro**, com a preferência do usuário salva entre execuções.

---

## ✨ Recursos implementados

- ✅ **Cadastro de tarefas** com **título**, **categoria**, **mensagem personalizada** e
  **data/horário** do lembrete.
- ✅ **Categorias prontas**: Beber Água, Estudar, Medicamento, Exercício e Compromisso.
- ✅ **Notificações locais agendadas** com **AlarmManager**
  (`setExactAndAllowWhileIdle`), disparadas no horário exato mesmo com o app fechado.
- ✅ A notificação exibe o **nome da tarefa** e uma **mensagem de lembrete personalizada**.
- ✅ **Tema Claro / Tema Escuro** com interruptor na barra superior, e o estado é
  **persistido** (a escolha é mantida ao reabrir o app). Toda a interface acompanha a mudança.
- ✅ **Marcar como concluída** (cancela o lembrete) e **remover tarefa** (cancela o alarme).
- ✅ **Reagendamento automático** dos lembretes após reiniciar o aparelho (`BootReceiver`).
- ✅ Solicitação da permissão de **notificações** (Android 13+) em tempo de execução.
- ✅ Persistência local simples via **SharedPreferences + JSON** (sem dependências extras).

---

## 🛠️ Tecnologias

| Item              | Versão / Tecnologia                 |
|-------------------|-------------------------------------|
| Linguagem         | Kotlin                              |
| UI                | Jetpack Compose + Material 3        |
| Arquitetura       | MVVM (ViewModel + StateFlow)        |
| Agendamento       | AlarmManager + BroadcastReceiver    |
| Notificações      | NotificationCompat / NotificationChannel |
| Persistência      | SharedPreferences (JSON)            |
| Build             | Gradle (Kotlin DSL) / AGP 9         |
| minSdk / targetSdk| 24 / 36                             |

---

## 📁 Estrutura do projeto

```
app/src/main/java/com/example/tarefas/
├── MainActivity.kt              # Activity principal; aplica o tema e pede permissão de notificação
├── TarefasApplication.kt        # Cria o canal de notificação ao iniciar o app
├── model/
│   └── Tarefa.kt                # Modelo de dados da tarefa
├── data/
│   └── RepositorioTarefas.kt    # Persistência (tarefas + preferência de tema)
├── notification/
│   ├── NotificacaoHelper.kt     # Criação do canal de notificação
│   ├── AgendadorLembrete.kt     # Agenda/cancela alarmes (AlarmManager)
│   ├── LembreteReceiver.kt      # Recebe o alarme e mostra a notificação
│   └── BootReceiver.kt          # Reagenda lembretes após reiniciar o aparelho
└── ui/
    ├── TarefaViewModel.kt       # Estado da tela e regras de negócio (MVVM)
    ├── TelaListaTarefas.kt      # Tela principal: lista, diálogo de cadastro, item
    └── theme/                   # Cores, tipografia e Theme (claro/escuro)
```

---

## ▶️ Como executar

### Pré-requisitos
- **Android Studio** (versão recente, com suporte a AGP 9).
- **JDK 17** (já incluído no Android Studio).
- Um **emulador** ou **dispositivo físico** com Android 7.0 (API 24) ou superior.

### Passos
1. **Clone** o repositório:
   ```bash
   git clone https://github.com/SEU-USUARIO/ListaDeTarefas.git
   ```
2. Abra a pasta do projeto no **Android Studio** (`File > Open`).
3. Aguarde o **Gradle Sync** baixar as dependências.
4. Selecione um emulador/dispositivo e clique em **▶ Run** (`Shift + F10`).

> Pela linha de comando (com o Gradle Wrapper incluído):
> ```bash
> ./gradlew assembleDebug      # gera o APK de debug
> ./gradlew installDebug       # instala no dispositivo/emulador conectado
> ```

---

## 📱 Como usar

1. Toque no botão flutuante **+** para criar uma nova tarefa.
2. Informe o **título**, escolha a **categoria**, digite uma **mensagem** (opcional) e
   toque em **Horário** para escolher a **data e a hora** do lembrete.
3. Toque em **Salvar**. No horário programado, o app emitirá uma **notificação local**
   com o nome da tarefa e a mensagem — **mesmo que o app esteja fechado**.
4. Use o **interruptor ☀️/🌙** na barra superior para alternar entre **Tema Claro** e
   **Tema Escuro**. A preferência é salva automaticamente.
5. Toque no círculo à esquerda para **marcar/desmarcar como concluída** ou no ícone de
   **lixeira** para **remover** a tarefa.

### ⚠️ Observações importantes
- No **Android 13+**, conceda a permissão de **notificações** quando solicitada.
- Em alguns aparelhos (Android 12+), pode ser necessário permitir **"Alarmes e
  lembretes"** nas configurações do app para que o horário seja **exato**. Caso o sistema
  não permita alarmes exatos, o app usa automaticamente um alarme aproximado.
- Para testar rapidamente, cadastre uma tarefa com horário **1 a 2 minutos no futuro**.

---

## 🎥 Vídeo de demonstração

📺 **YouTube (não listado):** _[adicione aqui o link do seu vídeo]_

O vídeo (até 5 minutos) demonstra:
- A criação de tarefas;
- O funcionamento das notificações no horário programado;
- A alternância entre os temas claro e escuro.

---

## 👤 Autor

Projeto acadêmico da disciplina de **Desenvolvimento para Dispositivos Móveis**.
