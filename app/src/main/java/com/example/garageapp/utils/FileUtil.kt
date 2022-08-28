package com.example.garageapp.utils

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore


class FileUtil {
    companion object {
        fun getRealPathFromURI(uri: Uri, contentResolver: ContentResolver): String? {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
            var path : String? = ""
            cursor?.let {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                it.moveToFirst()
                path = it.getString(columnIndex)
                it.close()
            }
            return path
        }
    }
}