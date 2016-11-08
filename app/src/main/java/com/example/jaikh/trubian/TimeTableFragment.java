package com.example.jaikh.trubian;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * Created by jaikh on 08-11-2016.
 */

public class TimeTableFragment extends Fragment {

    private AppCompatImageView time_table_iv;
    private StorageReference mStorageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_timetable, container, false);


    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Initialize ImageView
        time_table_iv = (AppCompatImageView) view.findViewById(R.id.timetable_iv);

        // Initialize Storage
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://trubian-2ca81.appspot.com/Time_Tables/");
        mStorageRef.child("TT5.PNG").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.with(getContext())
                        .load(uri)
                        .into(time_table_iv);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        // Setup any handles to view objects here

    }
}
