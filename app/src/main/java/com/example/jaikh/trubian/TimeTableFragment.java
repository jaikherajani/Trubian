package com.example.jaikh.trubian;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import static android.support.v4.content.PermissionChecker.checkCallingOrSelfPermission;

/**
 * Created by jaikh on 08-11-2016.
 */

public class TimeTableFragment extends Fragment {

    private static final int PERMS_REQUEST_CODE = 123;
    private AppCompatImageView time_table_iv;
    private StorageReference mStorageRef;
    private AppCompatButton Download;

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
        Download = (AppCompatButton) view.findViewById(R.id.download);

        // Initialize Storage
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://trubian-6f4e4.appspot.com/Time_Tables/");
        mStorageRef.child("CS141.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with(getContext())
                        .load(uri)
                        .into(time_table_iv);
                startDownload(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        // Setup any handles to view objects here

        //new DownloadFileFromURL().execute(mStorageRef.getDownloadUrl().toString());


    }

    private boolean hasPermissions() {

        int res = 0;
        String[] permissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (String perms : permissions) {
            res = checkCallingOrSelfPermission(getContext(), perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }

    private void requestPerms() {
        String[] permissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMS_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode) {
            case PERMS_REQUEST_CODE:

                for (int res : grantResults) {
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }

                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }

        if (allowed) {
            //user granted all permissions we can perform our task.
            makeFolder();
        } else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(getContext(), "Storage Permissions denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void makeFolder() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "TrubianAppDownloads");
        if (!file.exists()) {
            Boolean ff = file.mkdir();
            if (ff) {
                Toast.makeText(getActivity(), "Thanks for allowing permissions", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getActivity(), "Permissions have been denied. ", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void startDownload(final Uri uri) {
        //String url = uri.toString();
        Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermissions()) {
                    makeFolder();
                } else {
                    requestPerms();
                }
                //Toast.makeText(getContext(),uri.toString(), Toast.LENGTH_SHORT).show();
                new DownloadFileFromURL().execute(uri.toString());
            }
        });
    }
}

class DownloadFileFromURL extends AsyncTask<String, String, String> {

    /**
     * Before starting background thread
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Starting download");
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            System.out.println("Permission : true");
            return true;
        }
        return false;
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {

            // Get the directory for the user's public pictures directory.
            String root = Environment.getExternalStorageDirectory().toString();

            isExternalStorageWritable();

            System.out.println("Downloading");
            URL url = new URL(f_url[0]);

            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            OutputStream output = new FileOutputStream(root + "/Pictures/TimeTable.png");
            byte data[] = new byte[1024];

            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;

                // writing data to file
                output.write(data, 0, count);

            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }


    /**
     * After completing background task
     **/
    @Override
    protected void onPostExecute(String file_url) {
        System.out.println("Downloaded");
    }

}