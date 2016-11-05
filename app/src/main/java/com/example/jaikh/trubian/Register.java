package com.example.jaikh.trubian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;

public class Register extends AppCompatActivity {

    private CardView register_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_card = (CardView)findViewById(R.id.register_card);
    }

}
