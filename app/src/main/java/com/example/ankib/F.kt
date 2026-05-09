package com.example.ankib

/**
 * init("folder1/file1_en.ogg") parses to filename="folder1/file1_en.ogg", path=folder1, base=file1, suffix=en, ext=ogg
 */
data class F(
    val filename: String,
    val path: String,
    val base: String,
    val suffix: String,
    val ext: String
) {
    companion object {
        fun parse(fullPath: String): F {
            val file = java.io.File(fullPath)
            val filename = fullPath
            val path = file.parent ?: ""
            val nameWithExt = file.name
            val baseName = nameWithExt.substringBeforeLast('.')
            val ext = nameWithExt.substringAfterLast('.', "")
            
            val base = baseName.substringBeforeLast('_')
            val suffix = if (baseName.contains('_')) baseName.substringAfterLast('_') else ""
            
            return F(
                filename = filename,
                path = path,
                base = if (baseName.contains('_')) base else baseName,
                suffix = suffix,
                ext = ext
            )
        }
    }
}
