package com.mohaberabi.musik.data

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.mohaberabi.musik.MusikApplication
import com.mohaberabi.musik.R
import com.mohaberabi.musik.domain.Song
import com.mohaberabi.musik.domain.SongsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MusicPlayerService : Service() {
    companion object {
        const val PLAY_PAUSE = "play"
        const val NEXT = "next"
        const val PREVIOUS = "previous"
    }

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var index = 0

    private val _currentTrack = MutableStateFlow(SongsRepo.songs.first())
    val currentTrack = _currentTrack.asStateFlow()
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()


    private val binder = MusicPlayerBinder()

    override fun onBind(p0: Intent?): IBinder = binder

    inner class MusicPlayerBinder : Binder() {
        fun getService() = this@MusicPlayerService
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        intent?.let {
            when (it.action) {
                PLAY_PAUSE -> play()
                NEXT -> next()
                PREVIOUS -> previous()
            }
        }
        return START_STICKY
    }


    private fun createPendingIntent(
        pendingAction: String,
    ): PendingIntent {
        val intent = Intent(this, MusicPlayerBinder::class.java).apply {
            action = pendingAction
        }
        return PendingIntent.getService(
            this,
            pendingAction.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    private fun startForegroundWithNotification(
        song: Song,
    ) {
        val noti = createMusicNotification(song)
        startForeground(1, noti)
    }


    private fun createSongIcon(
        @DrawableRes res: Int,
    ) = BitmapFactory.decodeResource(resources, res)

    fun play() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.reset()
            mediaPlayer = MediaPlayer.create(this, _currentTrack.value.source)
            mediaPlayer.start()
        }
        _isPlaying.update { mediaPlayer.isPlaying }
        startForegroundWithNotification(_currentTrack.value)
    }


    fun next() {
        val size = SongsRepo.songs.size
        if (index == size - 1) {
            index = 0
        } else {
            index++
        }
        _currentTrack.update { SongsRepo.songs[index] }
        play()
    }

    fun previous() {
        if (index != 0) {
            index--
            _currentTrack.update { SongsRepo.songs[index] }
        }
        play()
    }

    private fun createMusicNotification(song: Song): Notification {
        val pause = createPendingIntent(PLAY_PAUSE)
        val next = createPendingIntent(NEXT)
        val prev = createPendingIntent(PREVIOUS)
        val songIcon = createSongIcon(song.image)
        val notification = NotificationCompat.Builder(
            this,
            MusikApplication.CHANNEL_ID,
        ).setContentTitle("Playing Now!")
            .setContentText(song.name)
            .addAction(R.drawable.ic_prev, PREVIOUS, prev)
            .addAction(R.drawable.ic_play, PLAY_PAUSE, pause)
            .addAction(R.drawable.ic_next, NEXT, next)
            .setLargeIcon(songIcon)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        return notification
    }
}