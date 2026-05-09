package com.example.ankib

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ankib.ui.theme.AnkiBTheme
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : ComponentActivity() {
    private lateinit var audioPlayer: AudioPlayer
    private lateinit var startUp: StartUp
    private lateinit var store: Store
    private lateinit var config: Config
    private lateinit var mediaSessionManager: MediaSessionManager
    private lateinit var headSetButton: HeadSetButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        initializeAssetsIfNeeded()

        audioPlayer = AudioPlayer(this)
        store = Store(this)
        headSetButton = HeadSetButton(audioPlayer, store)
        
        mediaSessionManager = MediaSessionManager(this, headSetButton)
        mediaSessionManager.init()

        startUp = StartUp(this)
        val folders = startUp.init()
        config = startUp.config
        
        audioPlayer.speed = config.speed

        enableEdgeToEdge()
        setContent {
            AnkiBTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        ConfigControls(config = config, onConfigChange = {
                            store.saveConfig(config)
                            audioPlayer.speed = config.speed
                        })
                    }
                ) { innerPadding ->
                    FolderList(
                        folders = folders,
                        audioPlayer = audioPlayer,
                        store = store,
                        config = config,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSessionManager.release()
    }

    private fun initializeAssetsIfNeeded() {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("assets_initialized", false)) {
            copyAssetsToFilesDir()
            prefs.edit().putBoolean("assets_initialized", true).apply()
        }
    }

    private fun copyAssetsToFilesDir() {
        try {
            val assetsList = assets.list("") ?: return
            for (assetName in assetsList) {
                if (assetName == "webkit" || assetName == "images" || assetName == "sounds") continue
                copyAssetRecursively(assetName, filesDir)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun copyAssetRecursively(assetPath: String, targetDir: File) {
        try {
            val subAssets = assets.list(assetPath)
            if (subAssets.isNullOrEmpty()) {
                copyFile(assetPath, File(targetDir, assetPath))
            } else {
                val newTargetDir = File(targetDir, assetPath)
                if (!newTargetDir.exists()) newTargetDir.mkdirs()
                for (asset in subAssets) {
                    copyAssetRecursively("$assetPath/$asset", targetDir)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun copyFile(assetPath: String, targetFile: File) {
        try {
            assets.open(assetPath).use { input ->
                targetFile.parentFile?.mkdirs()
                FileOutputStream(targetFile).use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: IOException) {}
    }
}

@Composable
fun ConfigControls(config: Config, onConfigChange: () -> Unit) {
    var speed by remember { mutableStateOf(config.speed) }
    var pause by remember { mutableStateOf(config.pause.toFloat()) }
    var sortReverse by remember { mutableStateOf(config.sortReverse) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Speed: ${"%.1f".format(speed)}")
            Slider(
                value = speed,
                onValueChange = { speed = it; config.speed = it; onConfigChange() },
                valueRange = 0.5f..2.0f,
                modifier = Modifier.weight(1f)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Pause: ${pause.toInt()}s")
            Slider(
                value = pause,
                onValueChange = { pause = it; config.pause = it.toInt(); onConfigChange() },
                valueRange = 0f..10f,
                modifier = Modifier.weight(1f)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Reverse Sort")
            Checkbox(
                checked = sortReverse,
                onCheckedChange = { sortReverse = it; config.sortReverse = it; onConfigChange() }
            )
        }
    }
}

@Composable
fun FolderList(
    folders: List<Folder>,
    audioPlayer: AudioPlayer,
    store: Store,
    config: Config,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(folders) { folder ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = folder.name)
                Row {
                    Button(onClick = { folder.play(audioPlayer, store, config) }) {
                        Text("Play")
                    }
                    Button(
                        onClick = { folder.pause(audioPlayer) },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Pause")
                    }
                }
            }
        }
    }
}
