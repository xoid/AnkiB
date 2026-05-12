package com.example.ankib

import android.content.Context

class StartUp(private val context: Context) {
    lateinit var config: Config
    private val store = Store(context)

    fun init(): List<Folder> {
        // Store.load Config
        config = store.loadConfig()
        
        // list application home dir for Folders, init all Folder
        val homeDir = context.filesDir
        Util.longToast( context, homeDir.absolutePath )
        val folders = homeDir.listFiles { file -> file.isDirectory } ?: emptyArray()
        return folders.map { Folder(it.absolutePath) }
    }
}
