package com.taras.pet.crytra

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.taras.pet.crytra.core.presentation.util.ObserveAsEvents
import com.taras.pet.crytra.core.presentation.util.toString
import com.taras.pet.crytra.crypto.presentation.coin_detail.CoinDetailsScreen
import com.taras.pet.crytra.crypto.presentation.coin_list.CoinListEvent
import com.taras.pet.crytra.crypto.presentation.coin_list.CoinListScreen
import com.taras.pet.crytra.crypto.presentation.coin_list.CoinListViewModel
import com.taras.pet.crytra.ui.theme.CrytraTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CrytraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = koinViewModel<CoinListViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    val context = LocalContext.current
                    ObserveAsEvents(events = viewModel.events) { event ->
                        when (event) {
                            is CoinListEvent.Error -> {
                                Toast.makeText(
                                    context,
                                    event.error.toString(context),
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                    }

                    when {
                        state.selectedCoin != null -> {
                            CoinDetailsScreen(
                                state = state,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        else -> {
                            CoinListScreen(
                                state = state,
                                modifier = Modifier.padding(innerPadding),
                                onAction = viewModel::onAction
                            )
                        }
                    }
                }
            }
        }
    }
}