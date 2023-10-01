package hu.bme.aut.android.ludocompose.features.local.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.ui.common.LoadingScreen
import hu.bme.aut.android.ludocompose.ui.graphics.drawGame
import hu.bme.aut.android.ludocompose.ui.model.toUiText

@Composable
fun GameScreen(
    onGameEnded: () -> Unit,
    gameViewModel: GameViewModel = hiltViewModel(),
) {
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LoadingScreen(state = state) {
        Column {
            key(state) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(shape = RectangleShape)
                        .background(color = Gray)
                        .drawWithContent {
                            val average = (size.width + size.height) / 2
                            drawGame(state.game!!, average)
                            drawContent()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.game!!.dice!!.value.toString(),
                        style = typography.displayLarge
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Button(onClick = { gameViewModel.select() }, enabled = state.isSelectEnabled) {
                    Text(text = stringResource(id = R.string.game_select))
                }
                Button(onClick = { gameViewModel.step { onGameEnded() } }) {
                    Text(text = stringResource(id = R.string.game_step))
                }
            }
        }
    }
}
