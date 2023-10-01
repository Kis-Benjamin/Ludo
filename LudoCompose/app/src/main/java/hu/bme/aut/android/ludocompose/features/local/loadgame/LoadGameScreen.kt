package hu.bme.aut.android.ludocompose.features.local.loadgame

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.ui.model.toUiText
import hu.bme.aut.android.ludocompose.ui.util.UiEvent
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun LoadGameScreen(
    snackbarHostState: SnackbarHostState,
    onSuccess: () -> Unit,
    loadGameViewModel: LoadGameViewModel = hiltViewModel()
) {
    val state by loadGameViewModel.state.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    loadGameViewModel.loadAll()

    LaunchedEffect(key1 = true) {
        loadGameViewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Success -> {
                    onSuccess()
                }

                is UiEvent.Failure -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(uiEvent.message.asString(context))
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(15.dp))
                .background(
                    color = if (!state.isLoading && !state.isError) {
                        colorScheme.secondaryContainer
                    } else {
                        colorScheme.background
                    }
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = colorScheme.secondaryContainer
                )
            } else if (state.isError) {
                Text(
                    text = state.error?.toUiText()?.asString(context)
                        ?: stringResource(id = R.string.unknown_error_message)
                )
            } else {
                if (state.games.isEmpty()) {
                    Text(text = stringResource(id = R.string.load_game_list_empty))
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(5.dp))
                    ) {
                        itemsIndexed(state.games, key = { _, game -> game.id }) { index, game ->
                            val isSelected = index == state.selectedIndex

                            ListItem(
                                headlineContent = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = game.name)
                                    }
                                },
                                supportingContent = {
                                    Text(
                                        text = stringResource(
                                            id = R.string.load_game_list_item_date,
                                            game.date
                                        )
                                    )
                                },
                                trailingContent = {
                                    IconButton(
                                        onClick = {
                                            loadGameViewModel.delete(game.id)
                                        }
                                    ) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                                    }
                                },
                                modifier = Modifier
                                    .clickable {
                                        loadGameViewModel.select(index)
                                    },
                                tonalElevation = if (isSelected) {
                                    10.dp
                                } else {
                                    0.dp
                                }
                            )
                            if (index != state.games.lastIndex) {
                                Divider(
                                    thickness = 2.dp,
                                    color = colorScheme.secondaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { loadGameViewModel.loadSelected() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
        ) {
            Text(text = stringResource(id = R.string.load_game_button))
        }
    }
}