package com.example.jaikh.trubian;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by jaikh on 30-10-2016.
 */

public class Trubian extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
