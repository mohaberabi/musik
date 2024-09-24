package com.mohaberabi.musik.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohaberabi.musik.domain.MusicPlayer
import com.mohaberabi.musik.domain.Song
import com.mohaberabi.musik.domain.SongsRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class MusicPlayerViewModel(
) : ViewModel() {


    private var isPlayingJob: Job? = null

    private val _song = MutableStateFlow(SongsRepo.songs.first())
    val song = _song.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()


    fun playingChanged(isPlay: Boolean) {
        _isPlaying.update { isPlay }
    }

    fun songChanged(newSong: Song) {
        _song.update { newSong }
    }
}