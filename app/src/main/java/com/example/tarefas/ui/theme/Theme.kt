package com.example.tarefas.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val EsquemaEscuro = darkColorScheme(
    primary = RoxoPrimarioEscuro,
    secondary = RoxoSecundarioEscuro,
    tertiary = RoxoTerciarioEscuro,
    background = FundoEscuro,
    surface = SuperficieEscura
)

private val EsquemaClaro = lightColorScheme(
    primary = RoxoPrimario,
    secondary = RoxoSecundario,
    tertiary = RoxoTerciario,
    background = FundoClaro,
    surface = SuperficieClara
)

/**
 * Tema do aplicativo. O parâmetro [temaEscuro] é controlado pelo usuário
 * através do interruptor na barra superior (e persistido no repositório),
 * por isso o padrão usa a preferência do sistema apenas como valor inicial.
 *
 * `dynamicColor` é desativado de propósito para que a diferença entre o
 * tema claro e o escuro fique sempre evidente na demonstração.
 */
@Composable
fun TarefasTheme(
    temaEscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val esquemaCores = if (temaEscuro) EsquemaEscuro else EsquemaClaro

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = esquemaCores.primary.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !temaEscuro
        }
    }

    MaterialTheme(
        colorScheme = esquemaCores,
        typography = Tipografia,
        content = content
    )
}
