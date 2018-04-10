package com.kpfu.khlopunov.sportgid.firebase;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kpfu.khlopunov.sportgid.activities.AddEventActivity;

/**
 * Created by hlopu on 28.02.2018.
 */

public class UploadImage {
    private static final UploadImage ourInstance = new UploadImage();

    public static UploadImage getInstance() {
        return ourInstance;
    }

    private UploadImage() {
    }

    public void uploadImageEvent(FireBaseCallback fireBaseCallback, Uri filePath) {
        if(filePath!= null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference();
//        Uri file = Uri.fromFile(new File(filePath));
            StorageReference imageRef = reference.child("images/places/" + filePath);

//        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
// создаем uploadTask посредством вызова метода putFile(), в качестве аргумента идет созданная нами ранее Uri
            UploadTask uploadTask = imageRef.putFile(filePath);
            uploadTask.addOnFailureListener(exception -> {
                fireBaseCallback.setFirebaseLink(null);
                System.out.println("FAILURE UPLOAD");
            })
                    .addOnSuccessListener(taskSnapshot -> {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        System.out.println("SUCCESSFUL UPLOAD");
                        System.out.println("URI " + downloadUrl);
                        //TODO спросить У Булата как красиво сделать
                        fireBaseCallback.setFirebaseLink(downloadUrl.toString());
                    });
        }
        else {
            fireBaseCallback.setFirebaseLink(AddEventActivity.NOT_SELECTED_IMAGE);
        }
    }
    public void uploadImage(FireBaseCallback fireBaseCallback, Uri filePath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference();
//        Uri file = Uri.fromFile(new File(filePath));
        StorageReference imageRef = reference.child("images/events/" + filePath);
// создаем uploadTask посредством вызова метода putFile(), в качестве аргумента идет созданная нами ранее Uri
        UploadTask uploadTask = imageRef.putFile(filePath);

        uploadTask.addOnFailureListener(exception -> {
            fireBaseCallback.setFirebaseLink(null);
            System.out.println("FAILURE UPLOAD");
        })
                .addOnSuccessListener(taskSnapshot -> {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    System.out.println("SUCCESSFUL UPLOAD");
                    System.out.println("URI "+downloadUrl);
                    //TODO спросить У Булата как красиво сделать
                    fireBaseCallback.setFirebaseLink(downloadUrl.toString());

                });
    }
}
