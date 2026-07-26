package com.example.trailverse_mobile_application.repository

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File

class CloudinaryRepository(private val context: Context) {
    private val client = OkHttpClient()
    private val cloudName = context.getString(
        context.resources.getIdentifier("cloudinary_cloud_name", "string", context.packageName)
    )
    private val uploadPreset = context.getString(
        context.resources.getIdentifier("cloudinary_upload_preset", "string", context.packageName)
    )

    suspend fun uploadImage(uri: Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            val file = uriToFile(uri)
            val url = "https://api.cloudinary.com/v1_1/$cloudName/image/upload"

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
                .addFormDataPart("upload_preset", uploadPreset)
                .build()

            val request = Request.Builder().url(url).post(requestBody).build()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Upload failed: ${response.code}"))
            }

            val body = response.body?.string() ?: return@withContext Result.failure(Exception("Empty response"))
            val secureUrl = JSONObject(body).getString("secure_url")
            Result.success(secureUrl)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            file@ context.cacheDir.listFiles { f -> f.name.startsWith("upload_") }?.forEach { it.delete() }
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Cannot open image")
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        tempFile.outputStream().use { output -> inputStream.copyTo(output) }
        inputStream.close()
        return tempFile
    }
}