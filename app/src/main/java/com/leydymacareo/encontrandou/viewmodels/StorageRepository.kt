package com.leydymacareo.encontrandou.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class StorageRepository {
    private val TAG = "StorageRepository";
    private val storage = FirebaseStorage.getInstance().reference

    fun copyUriToTempFile(context: Context, uri: Uri): File? {
        Log.i(this.TAG, "Creating temp file from uri: $uri")
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val tempFile = File.createTempFile("picked_", ".jpg", context.cacheDir)

        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }

        Log.i(this.TAG, "Tempfile created ${tempFile.toURI()}")

        return tempFile
    }

    fun subirImagen(
        context: Context,
        uri: Uri,
        tipo: String,
        id: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val file = copyUriToTempFile(context, uri)

        if (file == null || !file.exists() || file.length() == 0L) {
            onError(IOException("The file does not exist or is empty: ${uri.path}"))
            return
        }

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val nombreArchivo = "$tipo/${id}_$timestamp.jpg"
        val referencia = storage.child(nombreArchivo)
        val stream = FileInputStream(file)

        referencia.putStream(stream)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception ?: Exception("Unknown upload error")
                }
                referencia.downloadUrl
            }
            .addOnSuccessListener { downloadUri ->
                onSuccess(downloadUri.toString())
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }



}
