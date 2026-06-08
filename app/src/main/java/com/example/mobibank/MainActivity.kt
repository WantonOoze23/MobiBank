package com.example.mobibank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.data.local.datastore.TokenManager
import com.example.mobibank.ui.navigation.MobiBankNavHost
import com.example.mobibank.ui.navigation.Route
import kotlinx.coroutines.flow.first
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val tokenManager: TokenManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val startDestination = remember { mutableStateOf<String?>(null) }

                    LaunchedEffect(Unit) {
                        tokenManager.getToken.collect { token ->
                            if (token.isNullOrBlank()) {
                                if (startDestination.value == null) {
                                    startDestination.value = Route.LOGIN
                                } else {
                                    navController.navigate(Route.LOGIN) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            } else {
                                if (startDestination.value == null) {
                                    startDestination.value = Route.ACCOUNTS
                                }
                            }
                        }
                    }

                    val destination = startDestination.value
                    if (destination != null) {
                        MobiBankNavHost(
                            navController = navController,
                            startDestination = destination
                        )
                    }
                }
            }
        }
    }
}