package com.mohaberabi.musik.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mohaberabi.musik.R
import com.mohaberabi.musik.domain.Song


@Composable
fun MusicPlayerScreen(
    modifier: Modifier = Modifier,
    song: Song,
    playing: Boolean,
    onPlay: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
) {

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = song.image),
            contentDescription = "",
            modifier = Modifier
                .padding(16.dp)
                .height(200.dp)
                .fillMaxWidth()
        )
        Text(
            text = song.name,
        )

        Row {


            IconButton(
                onClick = {
                    onNext()
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "null"
                )
            }

            IconButton(
                onClick = {
                    onPlay()
                },
            ) {

                Icon(
                    painter = if (playing) painterResource(id = R.drawable.ic_pause) else painterResource(
                        id = R.drawable.ic_play
                    ),
                    contentDescription = "null"
                )
            }

            IconButton(
                onClick = {
                    onPrevious()
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_prev),
                    contentDescription = "null"
                )
            }
        }
    }
}