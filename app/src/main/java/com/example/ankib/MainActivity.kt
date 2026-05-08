package com.example.ankib

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ankib.ui.theme.AnkiBTheme
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        initializeAssetsIfNeeded()

        enableEdgeToEdge()
        setContent {
            AnkiBTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
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
                // Ignore standard Android asset folders
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
                // Try to open it as a file
                copyFile(assetPath, File(targetDir, assetPath))
            } else {
                // It's a directory
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
        } catch (e: IOException) {
            // This might happen if assetPath is a directory but list() returned empty
            // or other IO issues.
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AnkiBTheme {
        Greeting("Android")
    }
}
