package com.example.productkotlin.ui.album

import android.content.Context
import android.provider.MediaStore.Images
import android.provider.MediaStore.MediaColumns
import androidx.loader.content.CursorLoader

class PhotoDirectoryLoader : CursorLoader {


    private val IMAGE_PROJECTION = arrayOf(
        Images.Media._ID,
        Images.Media.DATA,
        Images.Media.BUCKET_ID,
        Images.Media.BUCKET_DISPLAY_NAME,
        Images.Media.DATE_ADDED,
        Images.Media.SIZE
    )

    constructor(context: Context, showGif: Boolean) : super(context) {
        projection = IMAGE_PROJECTION
        uri = Images.Media.EXTERNAL_CONTENT_URI
        sortOrder = Images.Media.DATE_ADDED + " DESC"
        selection =
            MediaColumns.MIME_TYPE + "=? or " + MediaColumns.MIME_TYPE + "=? or " + MediaColumns.MIME_TYPE + "=? " + if (showGif) "or " + MediaColumns.MIME_TYPE + "=?" else ""
        selectionArgs = if (showGif) {
            arrayOf("image/jpeg", "image/png", "image/jpg", "image/gif")
        } else {
            arrayOf("image/jpeg", "image/png", "image/jpg")
        }
    }


}