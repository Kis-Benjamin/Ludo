package hu.bme.aut.android.ludocompose.features.local.loadgame

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.ui.common.LoadingScreen
import hu.bme.aut.android.ludocompose.ui.common.UiEventHandler

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun LoadGameScreen(
    snackbarHostState: SnackbarHostState,
    onSuccess: () -> Unit,
    loadGameViewModel: LoadGameViewModel = hiltViewModel()
) {
    val loadingState by loadGameViewModel.loadingState.collectAsStateWithLifecycle()
    val state by loadGameViewModel.state.collectAsStateWithLifecycle()

    UiEventHandler(loadGameViewModel.uiEvent, snackbarHostState) {
        onSuccess()
    }

    LoadingScreen(loadingState) {
        Column {
            if (state.games.isEmpty()) {
                Text(text = stringResource(id = R.string.load_game_list_empty))
            } else {
                val shape = RoundedCornerShape(15.dp)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(15.dp)
                        .clip(shape)
                        .background(colorScheme.secondaryContainer, shape)
                ) {
                    itemsIndexed(state.games, key = { _, game -> game.id }) { index, game ->
                        val isSelected = index == state.selectedIndex

                        ListItem(
                            modifier = Modifier
                                .padding(5.dp)
                                .clip(shape)
                                .background(colorScheme.onSecondaryContainer, shape)
                                .clickable {
                                    loadGameViewModel.select(index)
                                },
                            headlineContent = {
                                Text(text = game.name)
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
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null
                                    )
                                }
                            },
                            tonalElevation = if (isSelected) {
                                50.dp
                            } else {
                                0.dp
                            }
                        )
                    }
                }
            }
            Button(
                onClick = { loadGameViewModel.loadSelected() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp, end = 50.dp, bottom = 30.dp)
            ) {
                Text(text = stringResource(id = R.string.load_game_button))
            }
        }
    }
}