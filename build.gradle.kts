// Build script de nível raiz (top-level).
// Os plugins são declarados aqui com `apply false` e aplicados no módulo :app.
// O AGP 9.x já traz suporte a Kotlin embutido, por isso só precisamos declarar
// o plugin do Compose Compiler além do plugin da aplicação Android.
plugins {
    id("com.android.application") version "9.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.10" apply false
}
