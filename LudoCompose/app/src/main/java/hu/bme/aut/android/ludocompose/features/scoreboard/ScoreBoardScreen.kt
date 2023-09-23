package hu.bme.aut.android.ludocompose.features.scoreboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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

@Composable
fun ScoreBoardScreen(
    snackbarHostState: SnackbarHostState,
    scoreBoardViewModel: ScoreBoardViewModel = hiltViewModel()
) {
    val state by scoreBoardViewModel.state.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    scoreBoardViewModel.loadAll()

    LaunchedEffect(key1 = true) {
        scoreBoardViewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Success -> {}

                is UiEvent.Failure -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(uiEvent.message.asString(context))
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
                .background(
                    color = if (!state.isLoading && !state.isError) {
                        colorScheme.secondaryContainer
                    } else {
                        colorScheme.background
                    }
                ),
            contentAlignment = Alignment.Center
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
                if (state.scores.isEmpty()) {
                    Text(text = stringResource(id = R.string.score_board_empty))
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(5.dp))
                    ) {
                        itemsIndexed(state.scores, key = { _, score -> score.id }) { index, score ->
                            ListItem(
                                headlineContent = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = score.name)
                                        Text(text = score.winCount)
                                    }
                                },
                                trailingContent = {
                                    IconButton(
                                        onClick = {
                                            scoreBoardViewModel.delete(score.id)
                                        }
                                    ) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                                    }
                                }
                            )
                            if (index != state.scores.lastIndex) {
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
    }
}
