package com.mohaberabi.musik.domain

interface MusicPlayer {


    fun play(song: Song)
    fun pause()
    fun stop()
    fun isPlaying(): Boolean
}