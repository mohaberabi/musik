package com.mohaberabi.musik

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mohaberabi.musik.data.AndroidMusicPlayer
import com.mohaberabi.musik.data.MusicPlayerService
import com.mohaberabi.musik.data.rememberMusicPlayer
import com.mohaberabi.musik.domain.SongsRepo
import com.mohaberabi.musik.presentation.MusicPlayerScreen
import com.mohaberabi.musik.presentation.MusicPlayerViewModel
import com.mohaberabi.musik.presentation.viewmodelFactory
import com.mohaberabi.musik.ui.theme.MusikTheme
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    private val viewmodel by viewModels<MusicPlayerViewModel>()
    private var service: MusicPlayerService? = null
    private var isBound: Boolean = false
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            service = (binder as MusicPlayerService.MusicPlayerBinder).getService()
            isBound = true



            lifecycleScope.launch {
                service!!.isPlaying.collect {
                    viewmodel.playingChanged(it)
                }
            }

            lifecycleScope.launch {
                service!!.currentTrack.collect {
                    viewmodel.songChanged(it)
                }
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val song by viewmodel.song.collectAsState()
            val playing by viewmodel.isPlaying.collectAsState()
            MusikTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    val launcher =
                        rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.RequestPermission(),
                        ) {
                        }
                    LaunchedEffect(
                        key1 = Unit,
                    ) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }

                    MusicPlayerScreen(
                        song = song,
                        playing = playing,
                        onNext = {
                            service?.next()
                        },
                        onPlay = {
                            bindMusicService()
                            service?.play()
                        },
                        onPrevious = {
                            service?.previous()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun bindMusicService() {
        val intent = Intent(
            this@MainActivity,
            MusicPlayerService::class.java
        )
        startService(intent)
        bindService(intent, connection, BIND_AUTO_CREATE)
    }
}

