package com.example.ankib

import com.example.ankib.Folder
import java.io.File

typealias Filename = String

data class File(
    val path: Filename,
    val filename: Filename,
    val basename: String,
    val suffix: String,
    val ext: String
) {
    constructor(path: Dir, filename: Filename) : this(
        path = path,
        filename = filename,
        basename = filename.substringAfterLast('/').substringBeforeLast('.').substringBeforeLast('_'),
        suffix = filename.substringAfterLast('/').substringBeforeLast('.').let {
            if (it.contains('_')) it.substringAfterLast('_') else ""
        },
        ext = filename.substringAfterLast('.', "")
    )
}