package com.example.ufimets.danceperson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TitleActivity extends AppCompatActivity {
    private static final String LOG_TAG = "title1";
    Button start_button;
    EditText user_name;
    String txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        start_button = findViewById(R.id.start_button);
        user_name = findViewById(R.id.user_name);



        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt = user_name.getText().toString();
                Log.i(LOG_TAG, txt);
                Intent intent = new Intent(TitleActivity.this, MainActivity.class);
                intent.putExtra("CODE", txt);
                startActivity(intent);
            }
        });

    }
}
