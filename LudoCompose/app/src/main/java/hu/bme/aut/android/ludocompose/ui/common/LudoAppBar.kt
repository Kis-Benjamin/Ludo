package hu.bme.aut.android.ludocompose.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.bme.aut.android.ludocompose.ui.theme.LudoComposeTheme

@ExperimentalMaterial3Api
@Composable
fun LudoAppBar(
    modifier: Modifier = Modifier,
    title: String,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        ) },
        navigationIcon = {},
        actions = {},
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Preview
@ExperimentalMaterial3Api
@Composable
fun LudoAppBar_Preview() {
    LudoComposeTheme {
        LudoAppBar(
            title = "Ludo",
        )
    }
}
