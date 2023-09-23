package hu.bme.aut.android.ludocompose.features.newgame

import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.ui.theme.LudoComposeTheme

@ExperimentalMaterial3Api
@Composable
fun PlayerCountDropDown(
    onPlayerCountSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = typography.labelLarge,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f, label = ""
    )

    val playerCounts = PlayerCount.entries

    var selectedPlayerCount by remember { mutableStateOf(playerCounts[0]) }

    val shape = RoundedCornerShape(5.dp)

    Surface(
        modifier = modifier
            .clip(shape = shape)
            .width(TextFieldDefaults.MinWidth)
            .background(colorScheme.background)
            .height(TextFieldDefaults.MinHeight)
            .clickable(enabled = enabled) { expanded = true },
        shape = shape
    ) {
        Row(
            modifier = modifier
                .width(TextFieldDefaults.MinWidth)
                .height(TextFieldDefaults.MinHeight)
                .clip(shape = shape),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                modifier = Modifier
                    .weight(weight = 8f),
                text = stringResource(id = selectedPlayerCount.title),
                style = style
            )
            IconButton(
                modifier = Modifier
                    .rotate(degrees = angle)
                    .weight(weight = 1.5f),
                onClick = { expanded = true }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
            DropdownMenu(
                modifier = modifier
                    .width(TextFieldDefaults.MinWidth),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                playerCounts.forEach { playerCount ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(id = playerCount.title),
                                style = style
                            )
                        },
                        onClick = {
                            expanded = false
                            selectedPlayerCount = playerCount
                            onPlayerCountSelected(playerCount.value)
                        },
                    )
                }
            }
        }
    }
}

enum class PlayerCount(
    @StringRes val title: Int,
    val value: Int,
) {
    Two(R.string.new_game_two_players,2),
    Three(R.string.new_game_three_players, 3),
    Four(R.string.new_game_four_players, 4),
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun PlayerCountDropDown_Preview() {
    LudoComposeTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlayerCountDropDown(
                onPlayerCountSelected = {
                    //printk(it)
                }
            )
        }
    }
}
