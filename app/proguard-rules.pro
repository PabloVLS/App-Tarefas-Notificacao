# Regras padrão de ProGuard/R8. O app não usa minify em release por padrão.
# Mantém os nomes das classes usadas por reflexão pelo Android (Receivers, Activity).
-keep class com.example.tarefas.** { *; }
