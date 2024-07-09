package com.example.mythesisapp.data.repository

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.UUID

class StorageRepository(
    firebaseStorage: FirebaseStorage,
) {
    private var imagesRef: StorageReference? = firebaseStorage.reference.child("images")

    fun addImage(
        bitmap: Bitmap,
        onSuccess: (String, String) -> Unit = { _, _ -> },
        onFailure: (String) -> Unit
    ) {
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val reference = imagesRef?.child(imageName)

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream)
        val data = byteArrayOutputStream.toByteArray()

        reference?.putBytes(data)?.addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString(), imageName)
            }.addOnFailureListener { exception ->
                onFailure(exception.message.orEmpty())
            }
        }?.addOnFailureListener {
            onFailure(it.message.orEmpty())
        }
    }


}