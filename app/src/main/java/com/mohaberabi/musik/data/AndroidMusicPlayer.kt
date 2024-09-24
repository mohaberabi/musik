package com.mohaberabi.musik.data

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mohaberabi.musik.domain.MusicPlayer
import com.mohaberabi.musik.domain.Song

class AndroidMusicPlayer(
    private val context: Context,
) : MusicPlayer {
    private var mediaPlayer: MediaPlayer? = null
    override fun play(song: Song) {

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(
                context, song.source,
            ).apply {
                start()
            }
        } else {
            mediaPlayer?.start()
        }

    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }
}

@Composable
fun rememberMusicPlayer(): MusicPlayer {
    val context = LocalContext.current
    return remember {
        AndroidMusicPlayer(context)
    }
}