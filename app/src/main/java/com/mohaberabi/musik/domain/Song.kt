package com.mohaberabi.musik.domain

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.mohaberabi.musik.R


data class Song(
    val id: String,
    @RawRes val source: Int,
    val name: String,
    @DrawableRes val image: Int
)


object SongsRepo {

    val songs = listOf(
        Song(
            "1",
            R.raw.song,
            "Eminem Superman High Quality 1 ",
            R.drawable.img
        ),
        Song(
            "2",
            R.raw.song,
            "Eminem Superman High Quality 2 ",
            R.drawable.img
        ),
        Song(
            "3",
            R.raw.song,
            "Eminem Superman High Quality 3 ",
            R.drawable.img
        ),
        Song(
            "4",
            R.raw.song,
            "Eminem Superman High Quality 4 ",
            R.drawable.img
        ),
        Song(
            "5",
            R.raw.song,
            "Eminem Superman High Quality 5 ",
            R.drawable.img
        ),
    )
}