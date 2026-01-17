package com.itirafapp.android.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.itirafapp.android.presentation.navigation.RootNavigation
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: Debug için Türkçe yapıldı, release'da kaldırılacak
        val locale = java.util.Locale("tr")
        java.util.Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)

        setContent {
            ItirafAppTheme {
                enableEdgeToEdge()
                RootNavigation()
            }
        }
    }
}